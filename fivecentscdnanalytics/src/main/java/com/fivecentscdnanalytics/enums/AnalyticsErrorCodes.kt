package com.fivecentscdnanalytics.enums

import com.fivecentscdnanalytics.data.ErrorCode
import com.fivecentscdnanalytics.features.errordetails.ErrorData


enum class AnalyticsErrorCodes(val errorCode: ErrorCode) {
    // TODO in the `additionalData`, we could describe why the message happened (e.g. you've been buffering for more than x minutes)
    ANALYTICS_QUALITY_CHANGE_THRESHOLD_EXCEEDED(ErrorCode(10000, "ANALYTICS_QUALITY_CHANGE_THRESHOLD_EXCEEDED", ErrorData())),
    ANALYTICS_BUFFERING_TIMEOUT_REACHED(ErrorCode(10001, "ANALYTICS_BUFFERING_TIMEOUT_REACHED", ErrorData())),
    ANALYTICS_VIDEOSTART_TIMEOUT_REACHED(ErrorCode(10002, "ANALYTICS_VIDEOSTART_TIMEOUT_REACHED", ErrorData()))
}
