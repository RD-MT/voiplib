package io.phone.build.sdk.voipsdkandroid.events

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.phone.build.sdk.voipsdkandroid.PIL
import io.phone.build.sdk.voipsdkandroid.events.Event.CallSessionEvent.*
import io.phone.build.sdk.voipsdkandroid.events.Event.CallSessionEvent.AttendedTransferEvent.*
import io.phone.build.sdk.voipsdkandroid.log
import kotlin.reflect.KClass

class EventsManager internal constructor(private val pil: PIL) {

    private var eventListeners = mutableListOf<PILEventListener>()

    fun listen(listener: PILEventListener) {
        if (eventListeners.contains(listener)) return

        eventListeners.add(listener)
    }

    fun stopListening(listener: PILEventListener) {
        eventListeners.remove(listener)
    }

    internal fun broadcast(event: Event) = GlobalScope.launch(Dispatchers.Main) {
        if (!pil.isStarted) {
            log("Not broadcasting event as pil is not properly started")
            return@launch
        }

        eventListeners.forEach {
            it.onEvent(event)
        }
    }

    internal fun broadcast(eventClass: KClass<out Event.CallSessionEvent>) {
        val state = pil.sessionState

        if (eventClass != CallDurationUpdated::class) {
            log("Broadcasting ${eventClass.qualifiedName}")
        }

        broadcast(
            when(eventClass) {
                OutgoingCallStarted::class -> OutgoingCallStarted(state)
                IncomingCallReceived::class -> IncomingCallReceived(state)
                CallEnded::class -> CallEnded(state)
                CallConnected::class -> CallConnected(state)
                AttendedTransferStarted::class -> AttendedTransferStarted(state)
                AttendedTransferConnected::class -> AttendedTransferConnected(state)
                AttendedTransferAborted::class -> AttendedTransferAborted(state)
                AttendedTransferEnded::class -> AttendedTransferEnded(state)
                AudioStateUpdated::class -> AudioStateUpdated(state)
                CallDurationUpdated::class -> CallDurationUpdated(state)
                CallStateUpdated::class -> CallStateUpdated(state)
                CallReleased::class -> CallReleased(state)
                else -> throw IllegalArgumentException("Attempted to broadcast an unregistered event (${eventClass.qualifiedName}), make sure to register in EventsManager")
            }
        )
    }
}
