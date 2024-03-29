package com.fivecentscdnanalytics.enums

import com.fivecentscdnanalytics.data.ErrorCode
import com.fivecentscdnanalytics.data.LegacyErrorData
import com.fivecentscdnanalytics.features.errordetails.ErrorData
import com.fivecentscdnanalytics.utils.topOfStacktrace
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.fivecentscdnanalytics.error.ExceptionMapper

class ExoPlayerExceptionMapper : ExceptionMapper<Throwable> {

    private val errorMessages = mapOf(
            -1 to "Unknown Error",
            0 to "Source Error",
            1 to "Render Error",
            2 to "Unexpected Error",
            3 to "Remote Error",
            4 to "Out of memory Error")

    override fun map(throwable: Throwable): ErrorCode {

        val type = getExceptionType(throwable)
        var message = errorMessages[type] ?: "Unknown Error"
        val legacyErrorData: LegacyErrorData

        when (val exception = throwable.cause ?: throwable) {
            is HttpDataSource.InvalidResponseCodeException -> {
                message += ": InvalidResponseCodeException"
                legacyErrorData = LegacyErrorData("Data Source request failed with HTTP status: " + exception.responseCode + " - " + exception.dataSpec.uri, exception.topOfStacktrace)
            }
            is HttpDataSource.InvalidContentTypeException -> {
                message += ": InvalidContentTypeException"
                legacyErrorData = LegacyErrorData("Invalid Content Type: " + exception.contentType, exception.topOfStacktrace)
            }
            is HttpDataSource.HttpDataSourceException -> {
                message += ": HttpDataSourceException"
                legacyErrorData = LegacyErrorData("Unable to connect: " + exception.dataSpec.uri, exception.topOfStacktrace)
            }
            is BehindLiveWindowException -> {
                message += ": BehindLiveWindowException"
                legacyErrorData = LegacyErrorData("Behind live window: required segments not available", exception.topOfStacktrace)
            }
            else -> legacyErrorData = LegacyErrorData(exception.message ?: "", exception.topOfStacktrace)
        }
        val errorData = ErrorData(legacyErrorData.msg, legacyErrorData.details.toList())
        return ErrorCode(type, message, errorData, legacyErrorData)
    }

    private fun getExceptionType(throwable: Throwable): Int {
        return when (throwable) {
            is ExoPlaybackException -> throwable.type
            else -> -1
        }
    }
}
