package io.phone.build.sdk.voipsdkandroid.call

import io.phone.build.sdk.voipsdkandroid.contacts.Contacts
import io.phone.build.sdk.voiplib.model.CallState
import io.phone.build.sdk.voiplib.model.CallState.*
import io.phone.build.sdk.voiplib.model.Direction
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

typealias VoipLibCall = io.phone.build.sdk.voiplib.model.Call

internal class CallFactory(private val contacts: Contacts) {

    fun make(voipLibCall: VoipLibCall?): Call? {
        val call = voipLibCall ?: return null
        val remoteParty = findAppropriateRemotePartyInformation(call)

        return Call(
            remoteParty.number,
            remoteParty.name,
            convertCallState(voipLibCall.state),
            if (voipLibCall.direction == Direction.INCOMING) CallDirection.INBOUND else CallDirection.OUTBOUND,
            call.duration,
            call.isOnHold,
            UUID.randomUUID().toString(),
            call.quality.average,
            call.quality.current,
            contacts.find(voipLibCall),
            voipLibCall.callId,
            voipLibCall.reason,
        )
    }

    private fun findAppropriateRemotePartyInformation(call: VoipLibCall): RemotePartyInformation {
        if (call.pAssertedIdentity.isNotBlank()) {
            extractCallerInformationFromAlternativeHeaders(call.pAssertedIdentity)?.let {
                return it
            }
        }

        if (call.remotePartyId.isNotBlank()) {
            extractCallerInformationFromAlternativeHeaders(call.remotePartyId)?.let {
                return it
            }
        }

        return RemotePartyInformation(call.displayName, call.phoneNumber)
    }

    private fun convertCallState(state: CallState): io.phone.build.sdk.voipsdkandroid.call.CallState = when (state) {
        Idle, IncomingReceived, OutgoingInit -> io.phone.build.sdk.voipsdkandroid.call.CallState.INITIALIZING
        OutgoingProgress, OutgoingRinging -> io.phone.build.sdk.voipsdkandroid.call.CallState.RINGING
        Pausing, Paused -> io.phone.build.sdk.voipsdkandroid.call.CallState.HELD_BY_LOCAL
        PausedByRemote -> io.phone.build.sdk.voipsdkandroid.call.CallState.HELD_BY_REMOTE
        OutgoingEarlyMedia, Connected, StreamsRunning, Referred, CallUpdatedByRemote, CallIncomingEarlyMedia,
        CallUpdating, CallEarlyUpdatedByRemote, CallEarlyUpdating, Resuming,
        -> io.phone.build.sdk.voipsdkandroid.call.CallState.CONNECTED
        Error, Unknown -> io.phone.build.sdk.voipsdkandroid.call.CallState.ERROR
        CallEnd, CallReleased -> io.phone.build.sdk.voipsdkandroid.call.CallState.ENDED
    }

    private fun extractCallerInformationFromAlternativeHeaders(header: String): RemotePartyInformation? {
        val data: List<String> = header.extractCaptureGroups("^\"(.+)\" <?sip:(.+)@")

        return if (data.size < 2) null else RemotePartyInformation(data[0], data[1])
    }

}

internal data class RemotePartyInformation(
    val name: String,
    val number: String
)

internal fun String.extractCaptureGroups(pattern: String): List<String> {
    val p: Pattern = Pattern.compile(pattern)
    val m: Matcher = p.matcher(this)

    if (!m.find()) {
        return ArrayList()
    }

    val matches: ArrayList<String> = ArrayList()

    var i = 1

    while (true) {
        try {
            val match: String = m.group(i) ?: return matches
            matches.add(match)
            i++
        } catch (e: Exception) {
            return matches
        }
    }

}