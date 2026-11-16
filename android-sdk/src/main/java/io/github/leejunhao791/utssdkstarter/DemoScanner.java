package io.github.leejunhao791.utssdkstarter;

import java.util.Objects;

/** Emits a sample barcode when the caller requests a simulated scan. */
public final class DemoScanner {
    private DemoScanListener listener;

    public synchronized boolean start(DemoScanListener listener) {
        Objects.requireNonNull(listener, "listener");
        if (this.listener != null) return false;

        this.listener = listener;
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
}
