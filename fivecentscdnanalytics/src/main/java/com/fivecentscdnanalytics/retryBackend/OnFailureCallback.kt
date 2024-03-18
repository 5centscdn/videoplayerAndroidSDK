package com.fivecentscdnanalytics.retryBackend

interface OnFailureCallback {
    fun onFailure(e: Exception, cancel: () -> Unit)
}
