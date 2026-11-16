package io.github.leejunhao791.utssdkstarter;

import java.util.ArrayList;
import java.util.List;

final class DemoScannerTest {
    public static void main(String[] args) {
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
        System.out.println("PASS DemoScanner lifecycle and callbacks");
    }

    private static void check(boolean expected, String message) {
        if (!expected) throw new AssertionError(message);
    }
}
