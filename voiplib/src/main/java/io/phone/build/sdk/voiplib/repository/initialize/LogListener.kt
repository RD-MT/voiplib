package io.phone.build.sdk.voiplib.repository.initialize


interface LogListener {
    fun onLogMessageWritten(lev: LogLevel, message: String)
}

enum class LogLevel {
    DEBUG, TRACE, MESSAGE, WARNING, ERROR, FATAL
}