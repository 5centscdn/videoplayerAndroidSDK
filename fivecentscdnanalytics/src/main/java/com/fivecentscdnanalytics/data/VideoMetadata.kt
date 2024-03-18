package com.fivecentscdnanalytics.data

data class VideoMetadata(
        var customContentTye: String? = null,
        var customVideoDurationMillis: Long? = 0L,
        var customEncodingVariant: String? = null,
        var customVideoLanguage: String? = null,
        var customVideoId: String? = null,
        var customVideoSeries: String? = null,
        var customVideoProducer: String? = null,
        var customVideoTitle: String? = null,
        var customVideoVariantName: String? = null,
        var customVideoVariant: String? = null
)
