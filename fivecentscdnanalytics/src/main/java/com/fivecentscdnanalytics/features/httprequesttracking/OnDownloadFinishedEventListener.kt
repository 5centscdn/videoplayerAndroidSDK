package com.fivecentscdnanalytics.features.httprequesttracking

import com.fivecentscdnanalytics.features.httprequesttracking.OnDownloadFinishedEventObject

interface OnDownloadFinishedEventListener {
    fun onDownloadFinished(event: OnDownloadFinishedEventObject)
}
