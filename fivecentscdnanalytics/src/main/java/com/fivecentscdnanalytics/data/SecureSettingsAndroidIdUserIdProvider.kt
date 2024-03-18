package com.fivecentscdnanalytics.data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class SecureSettingsAndroidIdUserIdProvider(val context: Context) : UserIdProvider {
    @SuppressLint("HardwareIds")
    override fun userId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}
