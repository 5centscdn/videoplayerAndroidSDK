package com.fivecentscdnanalytics.enums

import com.fivecentscdnanalytics.data.ErrorCode


enum class VideoStartFailedReason(val reason: String, val errorCode: ErrorCode?) {
    PAGE_CLOSED("PAGE_CLOSED", null),
    PLAYER_ERROR("PLAYER_ERROR", null),
    TIMEOUT("TIMEOUT", AnalyticsErrorCodes.ANALYTICS_VIDEOSTART_TIMEOUT_REACHED.errorCode),
    UNKNOWN("UNKNOWN", null)
}
