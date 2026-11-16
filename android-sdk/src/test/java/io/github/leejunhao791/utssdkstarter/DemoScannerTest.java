package io.github.leejunhao791.utssdkstarter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

final class DemoScannerTest {
    public static void main(String[] args) throws InterruptedException {
        DemoScanner scanner = new DemoScanner();
        List<String> received = new ArrayList<>();
        check(!scanner.simulateScan(), "Scanner starts idle");
        check(scanner.start(received::add), "First subscription starts");
        check(!scanner.start(code -> { throw new AssertionError("Duplicate callback"); }),
                "Second subscription is rejected");
        check(scanner.simulateScan(), "First scan emits");
        check(scanner.simulateScan(), "Second scan emits");
        check(received.size() == 2, "One callback per scan");
        check("DEMO-0001".equals(received.get(0)), "Expected demo payload");
        scanner.stop();
        scanner.stop();
        check(!scanner.simulateScan(), "No scan after release");
        check(received.size() == 2, "Released listener receives nothing");
        List<String> nextPage = new ArrayList<>();
        check(scanner.start(nextPage::add), "A new page can subscribe");
        check(scanner.simulateScan(), "Scan emits again after restart");
        check(received.size() == 2 && nextPage.size() == 1, "Only the new listener receives it");
        scanner.stop();
        verifyWorkerCallback(scanner);
        verifyStopDropsPendingScan(scanner);
        verifyRestartDropsPreviousScan(scanner);
        System.out.println("PASS DemoScanner lifecycle and callbacks");
    }

    private static void verifyWorkerCallback(DemoScanner scanner) throws InterruptedException {
        check(!scanner.simulateDelayedScan(), "An idle scanner rejects delayed requests");
        CountDownLatch callback = new CountDownLatch(1);
        AtomicReference<String> payload = new AtomicReference<>();
        AtomicReference<Thread> worker = new AtomicReference<>();
        check(scanner.start(code -> {
            payload.set(code);
            worker.set(Thread.currentThread());
            callback.countDown();
        }), "Subscribe to delayed results");
        check(scanner.simulateDelayedScan(), "Schedule a delayed scan");
        check(callback.await(5, TimeUnit.SECONDS), "Delayed listener receives a result");
        check("DEMO-0001".equals(payload.get()), "Expected delayed payload");
        check(worker.get() != Thread.currentThread(), "Delayed callback uses a worker thread");
        scanner.stop();
    }

    private static void verifyStopDropsPendingScan(DemoScanner scanner) throws InterruptedException {
        CountDownLatch callback = new CountDownLatch(1);
        check(scanner.start(code -> callback.countDown()), "Subscribe before stopping");
        check(scanner.simulateDelayedScan(), "Schedule a scan before stopping");
        scanner.stop();
        check(!callback.await(3, TimeUnit.SECONDS), "Stop suppresses pending results");
    }

    private static void verifyRestartDropsPreviousScan(DemoScanner scanner) throws InterruptedException {
        AtomicInteger results = new AtomicInteger();
        CountDownLatch first = new CountDownLatch(1);
        CountDownLatch extra = new CountDownLatch(2);
        DemoScanListener listener = code -> {
            results.incrementAndGet();
            first.countDown();
            extra.countDown();
        };
        check(scanner.start(listener), "Subscribe before restart");
        check(scanner.simulateDelayedScan(), "Schedule a scan in the first subscription");
        scanner.stop();
        check(scanner.start(listener), "Restart with the same listener object");
        check(scanner.simulateDelayedScan(), "Schedule a scan in the new subscription");
        check(first.await(5, TimeUnit.SECONDS), "New request still returns a result");
        check(!extra.await(3, TimeUnit.SECONDS) && results.get() == 1,
                "Only the new subscription's scan returns a result");
        scanner.stop();
    }

    private static void check(boolean expected, String message) {
        if (!expected) throw new AssertionError(message);
    }
}
