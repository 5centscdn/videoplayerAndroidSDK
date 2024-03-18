package com.fivecentscdnanalyticsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fivecentscdnanalytics.data.FCCDNRequest;
import com.fivecentscdnanalytics.collectors.ExoPlayerCollector;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.data.PlayerData;
import com.fivecentscdnanalytics.data.UserData;
import com.fivecentscdnanalytics.data.VideoMetadata;
import com.fivecentscdnanalytics.InitAnalytics;
import com.fivecentscdnanalytics.utils.DeviceUUIDFactory;
import com.fivecentscdnanalytics.utils.UserAgentGenerator;
import com.fivecentscdnanalytics.utils.ViewIDGenerator;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExoPlayer player;
    StyledPlayerView playerView;

    private ExoPlayerCollector exoPlayerCollector;
    private FCCDNAnalyticsConfig fccdnAnalyticsConfig;

    TextView logTV;

    AppCompatEditText hashIDET, titleET, tagsET, urlET;
    CheckBox showCV, showLog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hashIDET = findViewById(R.id.hash_id_et);
        titleET = findViewById(R.id.title_et);
        tagsET = findViewById(R.id.tags_et);
        urlET = findViewById(R.id.url_et);
        showCV = findViewById(R.id.show_cv);
        showLog = findViewById(R.id.show_log);

        Button playBtn = findViewById(R.id.play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnalytics();
            }
        });

        // Step Optional: initiate a TextView for logging
        logTV = findViewById(R.id.log_tv);

        // Step 1: Initiate StyledPLayerView

        playerView = findViewById(R.id.player_view);

        endTime = System.currentTimeMillis();
//        prepareAnalytics();
    }

    void setAnalytics(){
        String hashID = hashIDET.getText().toString();
        String title = titleET.getText().toString();
        String tags = tagsET.getText().toString();
        String url = urlET.getText().toString();

        String[] tagElements = tags.split(",");
        List<String> fixedLengthList = Arrays.asList(tagElements);
        ArrayList<String> tagList = new ArrayList<String>(fixedLengthList);

        String viewerID = new DeviceUUIDFactory(this).getDeviceUuid().toString();
        String viewID = new ViewIDGenerator().generate();
        String userAgent = new UserAgentGenerator(this).generateUserAgent();

        fccdnAnalyticsConfig = new FCCDNAnalyticsConfig("hash_id",this);
        fccdnAnalyticsConfig.setTitle("5centsCDN Analytics");

        FCCDNRequest fccdnRequest = new FCCDNRequest();
        fccdnRequest.setHashID(hashID);
        fccdnRequest.setTitle(title);
        fccdnRequest.setTags(tagList.toArray(new String[0]));
        fccdnRequest.setUrl(url);
        fccdnRequest.setShowCV(showCV.isChecked());
        fccdnRequest.setViewerID(viewerID);
        fccdnRequest.setViewID(viewID);
        fccdnRequest.setUserAgent(userAgent);
        fccdnRequest.setRequestType("");
        fccdnRequest.setReferrer("");

        fccdnRequest.setValueOne(endTime - startTime);

        fccdnRequest.setLogTV(logTV);
        fccdnRequest.setLog(showLog.isChecked());

        fccdnAnalyticsConfig.setFccdnRequest(fccdnRequest);
        fccdnAnalyticsConfig.setPlayerView(playerView);

        exoPlayerCollector = new ExoPlayerCollector(fccdnAnalyticsConfig, this);

        new InitAnalytics(player, exoPlayerCollector, this, playerView);


    }


    long endTime, startTime;
    @SuppressLint("SetTextI18n")
    void prepareAnalytics(){
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");

        // Step 2: Generate viewerID, viewID & userAgent from the custom libraries provided
        String viewerID = new DeviceUUIDFactory(this).getDeviceUuid().toString();
        String viewID = new ViewIDGenerator().generate();
        String userAgent = new UserAgentGenerator(this).generateUserAgent();


        // Step 3: initiate FCCDNAnalyticsConfig class & set FCCDNRequest values
        fccdnAnalyticsConfig = new FCCDNAnalyticsConfig("hash_id",this);
        fccdnAnalyticsConfig.setTitle("5centsCDN Analytics");

        FCCDNRequest fccdnRequest = new FCCDNRequest();
        fccdnRequest.setHashID("4gpuj6i3znoio7r4");
        fccdnRequest.setTitle("Sample Video Title");
        fccdnRequest.setTags(tags.toArray(new String[0]));
        fccdnRequest.setUrl("https://woj7lng8dg82-hls-live.5centscdn.com/103_push_2351_001/e1f28fb33effa6a53242a82acbb245de.sdp/playlist.m3u8");
        fccdnRequest.setShowCV(true);
        fccdnRequest.setViewerID(viewerID);
        fccdnRequest.setViewID(viewID);
        fccdnRequest.setUserAgent(userAgent);
        fccdnRequest.setRequestType("");
        fccdnRequest.setReferrer("");

        // Step 4: set the Activity loading time as value1 initially. It will be replaced internally with respected value as the player progresses.
        fccdnRequest.setValueOne(endTime - startTime);

        // Step 5: Set a TextView for viewing the logs of API Request
        fccdnRequest.setLogTV(logTV);
        fccdnRequest.setLog(true);

        // Step 6: Set a the FCCDNRequest & StyledPlayerView in the FCCDNAnalyticsConfig
        fccdnAnalyticsConfig.setFccdnRequest(fccdnRequest);
        fccdnAnalyticsConfig.setPlayerView(playerView);


        // Step 7: Create Analytics Collector
        exoPlayerCollector = new ExoPlayerCollector(fccdnAnalyticsConfig, this);

        // Step 8: Init Analytics
        new InitAnalytics(player, exoPlayerCollector, this, playerView);

    }
}