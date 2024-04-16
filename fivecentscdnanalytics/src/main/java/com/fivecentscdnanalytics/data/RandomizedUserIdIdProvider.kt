package com.fivecentscdnanalytics.data

import java.util.UUID

class RandomizedUserIdIdProvider : UserIdProvider {
    private val userId: String = UUID.randomUUID().toString()

    override fun userId(): String = userId
}
