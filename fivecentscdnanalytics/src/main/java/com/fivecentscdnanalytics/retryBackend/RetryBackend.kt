package com.fivecentscdnanalytics.retryBackend

import android.os.Handler
import android.os.SystemClock
import com.fivecentscdnanalytics.data.AdEventData
import com.fivecentscdnanalytics.data.Backend
import com.fivecentscdnanalytics.data.CallbackBackend
import com.fivecentscdnanalytics.data.EventData
import com.fivecentscdnanalytics.utils.FCCLog
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Date
import kotlin.Exception
import okhttp3.internal.http2.StreamResetException

class RetryBackend(private val next: CallbackBackend, private val scheduleSampleHandler: Handler) :
    Backend {

    private val TAG = "RetryBackend"
    private var retryDateToken: Date? = null
    private val retryQueue = RetryQueue()
    fun getNextScheduledTime() = retryQueue.getNextScheduleTime()

    override fun send(eventData: EventData) {
        scheduleSample(RetrySample(eventData, 0, Date(), 0))
    }

    override fun sendAd(eventData: AdEventData) {
        scheduleSample(RetrySample(eventData, 0, Date(), 0))
    }

    private fun scheduleSample(retrySample: RetrySample<Any>) {
        val callback = object : OnFailureCallback {
            override fun onFailure(e: Exception, cancel: () -> Unit) {
                if (e is SocketTimeoutException || e is ConnectException || e is StreamResetException || e is UnknownHostException) {
                    cancel()
                    retryQueue.addSample(retrySample)
                    processQueuedSamples()
                }
            }
        }

        if (retrySample.eventData is EventData) {
           FCCLog.d(TAG, "sending sample ${retrySample.eventData?.sequenceNumber} retry ${retrySample.retry}")
            retrySample.eventData.retryCount = retrySample.retry
            this.next.send(retrySample.eventData, callback)
        } else if (retrySample.eventData is AdEventData) {
            FCCLog.d(TAG, "sending ad sample ${retrySample.eventData?.adId} retry ${retrySample.retry}")
            retrySample.eventData.retryCount = retrySample.retry
            this.next.sendAd(retrySample.eventData, callback)
        }
    }

    @Synchronized
    fun processQueuedSamples() {
        try {
            val nextScheduledTime = getNextScheduledTime()
            if (nextScheduledTime != null || retryDateToken != nextScheduledTime) {
                if (retryDateToken != null) {
                    scheduleSampleHandler.removeCallbacks(sendNextSampleRunnable, retryDateToken)
                }
                retryDateToken = nextScheduledTime
                val delay = maxOf((nextScheduledTime?.time ?: 0) - Date().time, 0) // to prevent negative delay
                scheduleSampleHandler.postAtTime(sendNextSampleRunnable, retryDateToken, SystemClock.uptimeMillis() + delay)
            }
        } catch (e: Exception) {
            FCCLog.e(TAG, "processQueuedSamples() threw an unexpected exception: ${e.message}", e)
        }
    }

    // https://developer.android.com/reference/java/lang/Runnable
    private val sendNextSampleRunnable = Runnable {
        try {
            val retrySample = retryQueue.getNextSampleOrNull()
            if (retrySample != null) {
                scheduleSample(retrySample)
            }
            retryDateToken = null
            processQueuedSamples()
        } catch (e: Exception) {
            FCCLog.e(TAG, "processSampleRunnable() threw an unexpected exception: ${e.message}", e)
        }
    }

    protected fun finalize() { // destroys an instance of RetryBackend
        FCCLog.d(TAG, "finalize")
        scheduleSampleHandler.removeCallbacksAndMessages(null) // removes all callbacks and messages
    }
}
