package com.fivecentscdnanalytics.error

import com.fivecentscdnanalytics.data.ErrorCode


interface ExceptionMapper<in T> {
    fun map(throwable: T): ErrorCode
}
