package com.fivecentscdnanalytics.features.errordetails

import com.fivecentscdnanalytics.features.httprequesttracking.HttpRequest
import com.fivecentscdnanalytics.utils.Util


data class ErrorDetail(val platform: String,
//                       val licenseKey: String,
                       val domain: String, val impressionId: String, val errorId: Long, val timestamp: Long, val code: Int?, val message: String?, val data: ErrorData, val httpRequests: List<HttpRequest>?, val analyticsVersion: String = Util.getAnalyticsVersion())
