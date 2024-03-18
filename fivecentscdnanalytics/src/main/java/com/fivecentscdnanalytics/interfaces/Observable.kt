package com.fivecentscdnanalytics.interfaces

interface Observable<TListener> {
    fun subscribe(listener: TListener)
    fun unsubscribe(listener: TListener)
}
