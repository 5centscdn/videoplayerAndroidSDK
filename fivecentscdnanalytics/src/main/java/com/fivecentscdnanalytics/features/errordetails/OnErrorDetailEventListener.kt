package com.fivecentscdnanalytics.features.errordetails

interface OnErrorDetailEventListener {
    fun onError(code: Int?, message: String?, errorData: ErrorData?)
}
