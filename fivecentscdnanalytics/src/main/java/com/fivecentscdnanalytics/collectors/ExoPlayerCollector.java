package com.fivecentscdnanalytics.collectors;

import android.content.Context;

import com.fivecentscdnanalytics.FCCDNAnalytics;
import com.fivecentscdnanalytics.adapters.ExoPlayerAdapter;
import com.fivecentscdnanalytics.adapters.PlayerAdapter;
import com.fivecentscdnanalytics.collectors.Collector;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.features.ExoPlayerFeatureFactory;
import com.fivecentscdnanalytics.features.FeatureFactory;
import com.fivecentscdnanalytics.utils.ExoUtil;
import com.google.android.exoplayer2.ExoPlayer;

import com.fivecentscdnanalytics.collectors.DefaultCollector;
import org.jetbrains.annotations.NotNull;

public class ExoPlayerCollector extends DefaultCollector<ExoPlayer>
        implements Collector<ExoPlayer> {

    Context context;

    public ExoPlayerCollector(FCCDNAnalyticsConfig fccdnAnalyticsConfig, Context context) {
        super(
                Companion.createAnalytics(
                        fccdnAnalyticsConfig, context, ExoUtil.getUserAgent(context)));
        this.context = context;
    }

    @Deprecated
    public ExoPlayerCollector(FCCDNAnalyticsConfig fccdnAnalyticsConfig) {
        this(fccdnAnalyticsConfig, fccdnAnalyticsConfig.getContext());
    }

    @NotNull
    @Override
    protected PlayerAdapter createAdapter(
            ExoPlayer exoPlayer, @NotNull FCCDNAnalytics analytics) {
        FeatureFactory featureFactory = new ExoPlayerFeatureFactory(analytics, exoPlayer);
        return new ExoPlayerAdapter(
                exoPlayer,
                analytics.getConfig(),
                analytics.getPlayerStateMachine(),
                featureFactory);
    }
}
