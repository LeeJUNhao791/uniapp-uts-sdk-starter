package io.github.leejunhao791.utssdkstarter;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Emits a sample barcode when the caller requests a simulated scan. */
public final class DemoScanner {
    private static final ScheduledExecutorService SCAN_WORKER =
            Executors.newSingleThreadScheduledExecutor(task -> {
                Thread worker = new Thread(task, "demo-scan-worker");
                worker.setDaemon(true);
                return worker;
            });

    private DemoScanListener listener;
    private long subscriptionId;

    public synchronized boolean start(DemoScanListener listener) {
        Objects.requireNonNull(listener, "listener");
        if (this.listener != null) return false;

        this.listener = listener;
        subscriptionId++;
        return true;
    }

    public synchronized boolean simulateScan() {
        if (listener == null) return false;

        listener.onScan("DEMO-0001");
        return true;
    }

    public synchronized void stop() {
        listener = null;
    }

    public synchronized boolean simulateDelayedScan() {
        if (listener == null) return false;

        long requestSubscription = subscriptionId;
        SCAN_WORKER.schedule(() -> deliverDelayedScan(requestSubscription), 2, TimeUnit.SECONDS);
        return true;
    }

    private synchronized void deliverDelayedScan(long requestSubscription) {
        if (requestSubscription == subscriptionId) simulateScan();
    }
}
