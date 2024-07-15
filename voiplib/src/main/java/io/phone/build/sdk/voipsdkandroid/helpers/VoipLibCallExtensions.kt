package io.phone.build.sdk.voipsdkandroid.helpers

import io.phone.build.sdk.voiplib.model.Call

internal val Call.identifier: String
    get() = libCall.hashCode().toString()