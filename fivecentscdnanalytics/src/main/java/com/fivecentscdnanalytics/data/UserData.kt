package com.fivecentscdnanalytics.data

data class UserData(
        var customUserId: String? = null,
        var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var profileImage: String? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var city: String? = null,
        var state: String? = null,
        var country: String? = null,
        var zipCode: String? = null
)
