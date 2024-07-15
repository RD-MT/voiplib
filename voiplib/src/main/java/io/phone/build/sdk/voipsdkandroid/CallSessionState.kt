package io.phone.build.sdk.voipsdkandroid

import io.phone.build.sdk.voipsdkandroid.audio.AudioState
import io.phone.build.sdk.voipsdkandroid.call.Call

data class CallSessionState(
    val activeCall: Call?,
    val inactiveCall: Call?,
    val audioState: AudioState
)