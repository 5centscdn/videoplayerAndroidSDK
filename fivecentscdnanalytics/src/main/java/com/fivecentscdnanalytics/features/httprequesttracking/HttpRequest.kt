package com.fivecentscdnanalytics.features.httprequesttracking

import com.fivecentscdnanalytics.features.httprequesttracking.HttpRequestType

data class HttpRequest(
    val timestamp: Long,
    val type: HttpRequestType,
    val url: String?,
    val lastRedirectLocation: String?,
    val httpStatus: Int,
    /**
     * Total time elapsed since the request was opened (including TTFB).
     */
    val downloadTime: Long,
    val timeToFirstByte: Long?,
    val size: Long?,
    val success: Boolean
)
