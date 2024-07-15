package io.phone.build.sdk.voiplib.config

import io.phone.build.sdk.voiplib.model.Codec
import io.phone.build.sdk.voiplib.model.Codec.*
import io.phone.build.sdk.voiplib.repository.initialize.CallListener
import io.phone.build.sdk.voiplib.repository.initialize.LogListener
import io.phone.build.sdk.voipsdkandroid.logging.LogManager
import io.phone.build.sdk.voipsdkandroid.service.LicenceRequest

typealias GlobalStateCallback = () -> Unit

data class Config(
        val callListener: CallListener = object : CallListener {},
        val stun: String? = null,
        val ring: String? = null,
        val logListener: LogListener? = null,
        val codecs: Array<Codec> = arrayOf(
                G722,
                G729,
                GSM,
                ILBC,
                ISAC,
                L16,
                OPUS,
                PCMA,
                PCMU,
                SPEEX,
        ),
        val userAgent: String = "AndroidVoIPLib",
        val onReady: GlobalStateCallback = {},
        val onDestroy: GlobalStateCallback = {},
        val onLicence: suspend (licenceKey: String, accessToken: String) -> Boolean = { _, _ -> true }
)