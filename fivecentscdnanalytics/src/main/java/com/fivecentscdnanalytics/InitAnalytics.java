package com.fivecentscdnanalytics;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fivecentscdnanalytics.collectors.ExoPlayerCollector;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.util.Timer;
import java.util.TimerTask;

public class InitAnalytics {
    private static final String TAG = "InitAnalytics";
    private ExoPlayer exoPlayer;
    private ExoPlayerCollector exoPlayerCollector;
    private DefaultBandwidthMeter bandwidthMeter;
    private Context context;

    StyledPlayerView playerView;
    private Timer timer;
    private final long INTERVAL_MS = 9999; // 10 seconds
    private long totalBytes = 0;

    public InitAnalytics(ExoPlayer exoPlayer, ExoPlayerCollector exoPlayerCollector, Context context, StyledPlayerView playerView) {
        this.exoPlayer = exoPlayer;
        this.exoPlayerCollector = exoPlayerCollector;
        this.context = context;
        this.playerView = playerView;

        bandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
        exoPlayer = new ExoPlayer.Builder(context).setBandwidthMeter(bandwidthMeter).build();

        bandwidthMeter.addEventListener(new Handler(Looper.getMainLooper()), new BandwidthMeter.EventListener() {
            @Override
            public void onBandwidthSample(int elapsedMs, long bytesTransferred, long bitrateEstimate) {
                Log.e("here ", "elapsedMs: " + elapsedMs + " bytesTransferred: " + bytesTransferred + " bitrateEstimate: " + bitrateEstimate);
                totalBytes += bytesTransferred;
            }
        });

        exoPlayerCollector.attachPlayer(exoPlayer);
        playerView.setPlayer(exoPlayer);

        Uri videoUri = Uri.parse(exoPlayerCollector.getConfig().getFccdnRequest().getUrl());

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

        // Start the timer when you start playing the video
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Perform your action using the totalBytes downloaded within the 10-second interval
                performAction(totalBytes);

                // Reset the totalBytes for the next interval
                totalBytes = 0;
                if (exoPlayerCollector.getConfig().getFccdnRequest().isPlaying() ||  exoPlayerCollector.getConfig().getFccdnRequest().isPlayerError()){
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }

            }
        }, INTERVAL_MS, INTERVAL_MS);

    }
    private void performAction(double downloadedData) {
        Log.e(TAG, "performAction: " + downloadedData );
        exoPlayerCollector.getConfig().getFccdnRequest().setBytes((long) downloadedData);
    }
}
