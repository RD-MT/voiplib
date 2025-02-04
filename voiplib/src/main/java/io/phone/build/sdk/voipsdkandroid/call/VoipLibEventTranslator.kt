package io.phone.build.sdk.voipsdkandroid.call

import org.linphone.core.AudioDevice
import io.phone.build.sdk.voipsdkandroid.PIL
import io.phone.build.sdk.voipsdkandroid.events.Event.CallSessionEvent.*
import io.phone.build.sdk.voipsdkandroid.events.Event.CallSessionEvent.AttendedTransferEvent.*
import io.phone.build.sdk.voipsdkandroid.events.EventsManager
import io.phone.build.sdk.voipsdkandroid.helpers.identifier
import io.phone.build.sdk.voipsdkandroid.log
import io.phone.build.sdk.voiplib.model.Call
import io.phone.build.sdk.voiplib.repository.initialize.CallListener

/**
 * Responsible for taking regular VoipLib events and translating them into a PIL Event.
 *
 */
internal class VoipLibEventTranslator(
    private val pil: PIL,
    private val calls: Calls,
    private val events: EventsManager
) : CallListener {

    override fun incomingCallReceived(call: Call) {
        log("incomingCallReceived: ${call.identifier}")

        if (calls.isInCall) {
            log("Ignoring incoming call (${call.identifier}) as we are in a call already")
            return
        }

        log("Setting up incoming call (${call.identifier}")

        calls.add(call)

        events.broadcast(IncomingCallReceived::class)
    }

    override fun outgoingCallCreated(call: Call) {
        log("outgoingCallCreated: ${call.identifier}")

        calls.add(call)

        if (calls.isInTransfer) {
            events.broadcast(AttendedTransferStarted::class)
        } else {
            events.broadcast(OutgoingCallStarted::class)
        }
    }

    override fun callConnected(call: Call) {
        log("callConnected: ${call.identifier}")

        pil.androidCallFramework.connection?.setActive()

        events.broadcast(
            if (!calls.isInTransfer)
                CallConnected::class
            else
                AttendedTransferConnected::class
        )
    }

    override fun callEnded(call: Call) {
        log("callEnded: ${call.identifier}")

        val currentSessionState = pil.sessionState
        val isInTransfer = calls.isInTransfer

        calls.removeCall(call)

        if (isInTransfer) {
            events.broadcast(AttendedTransferAborted(currentSessionState))
        } else {
            events.broadcast(CallEnded(currentSessionState))
        }
    }

    override fun attendedTransferMerged(call: Call) {
        log("attendedTransferMerged: ${call.identifier}")

        val currentSessionState = pil.sessionState

        calls.removeCall(call)

        events.broadcast(AttendedTransferEnded(currentSessionState))
    }

    override fun callReleased(call: Call) {
        pil.platformIntegrator.notifyIfMissedCall(call)
        pil.androidCallFramework.prune()
    }

    override fun streamsStarted() {
        log("Streams have started, properly routing audio.")
        pil.androidCallFramework.connection?.updateCurrentRouteBasedOnAudioState()
    }

    override fun availableAudioDevicesUpdated() {
        log("Available audio devices have been updated, sync with current selection.")
        pil.androidCallFramework.connection?.updateCurrentRouteBasedOnAudioState()
    }

    override fun currentAudioDeviceHasChanged(audioDevice: AudioDevice) {
        log("Audio device has been changed to [${audioDevice.deviceName}]")
    }

    override fun callUpdated(call: Call) = events.broadcast(CallStateUpdated::class)
}