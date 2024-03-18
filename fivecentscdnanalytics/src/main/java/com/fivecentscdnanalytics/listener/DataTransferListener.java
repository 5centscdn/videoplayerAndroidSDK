package com.fivecentscdnanalytics.listener;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;

public class DataTransferListener implements TransferListener {
    private long totalBytesTransferred = 0;

    @Override
    public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {
        // No action needed
    }

    @Override
    public void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {
        // No action needed
    }

    @Override
    public void onBytesTransferred(DataSource source, DataSpec dataSpec, boolean isNetwork, int bytesTransferred) {
        // If the transfer is network, update the total bytes transferred
        if (isNetwork) {
            totalBytesTransferred += bytesTransferred;
        }
    }

    @Override
    public void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {
        // No action needed
    }

    public long getTotalBytesTransferred() {
        return totalBytesTransferred;
    }
}
