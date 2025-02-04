package io.phone.build.sdk.voipsdkandroid.call

import android.text.format.DateUtils
import io.phone.build.sdk.voipsdkandroid.contacts.Contact

data class Call(
    val remoteNumber: String,
    val displayName: String,
    val state: CallState,
    val direction: CallDirection,
    val duration: Int,
    val isOnHold: Boolean,
    val uuid: String,
    val mos: Float,
    val currentMos: Float,
    val contact: Contact?,
    val callId: String,
    val reason: String,
) {
    val remotePartyHeading: String
        get() {
            if (contact != null) {
                return contact.name
            }

            if (displayName.isNotBlank()) {
                return displayName
            }

            return remoteNumber
        }

    val remotePartySubheading: String
        get() {
            if (contact != null || displayName.isNotBlank()) {
                return remoteNumber
            }

            return ""
        }

    val prettyDuration: String
        get() {
            return DateUtils.formatElapsedTime(duration.toLong())
        }

    val prettyRemoteParty: String
        get() = when {
            remotePartySubheading.isBlank() -> remotePartyHeading
            else -> "$remotePartyHeading ($remotePartySubheading)"
        }
}
