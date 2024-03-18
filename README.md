# 5centsCDN Analytics Demo

Steps to use 5centsCDN Analytics Library:

Step 0: Add the library in your app
```

```

Step 1: Initiate StyledPlayerView
```
playerView = findViewById(R.id.player_view);
```


Step 2: Generate viewerID, viewID & userAgent from the custom libraries provided
```
String viewerID = new DeviceUUIDFactory(this).getDeviceUuid().toString();
String viewID = new ViewIDGenerator().generate();
String userAgent = new UserAgentGenerator(this).generateUserAgent();
```



Step 3: initiate FCCDNAnalyticsConfig class & set FCCDNRequest values
```
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
```


Step 4: set the Activity loading time as value1 initially. It will be replaced internally with respected value as the player progresses.
```
fccdnRequest.setValueOne(endTime - startTime);
```


Step 5: Set a TextView for viewing the logs of API Request
```
fccdnRequest.setLogTV(logTV);
fccdnRequest.setLog(true);
```


Step 6: Set a the FCCDNRequest & StyledPlayerView in the FCCDNAnalyticsConfig
```
fccdnAnalyticsConfig.setFccdnRequest(fccdnRequest);
fccdnAnalyticsConfig.setPlayerView(playerView);
```


Step 7: Create Analytics Collector
```
exoPlayerCollector = new ExoPlayerCollector(fccdnAnalyticsConfig, this);
```


Step 8: Init Analytics
```
new InitAnalytics(player, exoPlayerCollector, this, playerView);
```
