package io.phone.build.sdk.voipsdkandroid.logging

fun interface Logger {
    fun onLogReceived(message: String, level: LogLevel)
}
