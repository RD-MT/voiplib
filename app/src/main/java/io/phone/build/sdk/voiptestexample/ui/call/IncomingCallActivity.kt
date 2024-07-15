package io.phone.build.sdk.voiptestexample.ui.call

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_call.callSubtitle
import kotlinx.android.synthetic.main.activity_call.callTitle
import kotlinx.android.synthetic.main.activity_incoming_call.answerCallButton
import kotlinx.android.synthetic.main.activity_incoming_call.declineCallButton
import io.phone.build.sdk.voiptestexample.R
import io.phone.build.sdk.voipsdkandroid.PIL
import io.phone.build.sdk.voipsdkandroid.android.CallScreenLifecycleObserver
import io.phone.build.sdk.voipsdkandroid.events.Event
import io.phone.build.sdk.voipsdkandroid.events.PILEventListener

class IncomingCallActivity : AppCompatActivity(), PILEventListener {

    private val pil by lazy { PIL.instance }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)
        lifecycle.addObserver(CallScreenLifecycleObserver(this))

        answerCallButton.setOnClickListener {
            pil.actions.answer()
        }

        declineCallButton.setOnClickListener {
            pil.actions.decline()
        }
    }

    override fun onResume() {
        super.onResume()
        displayCall()
    }

    private fun displayCall() {
        val call = pil.calls.active ?: return

        callTitle.text = call.remotePartyHeading
        callSubtitle.text = call.remotePartySubheading
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.CallSessionEvent.CallEnded -> finish()
            is Event.CallSessionEvent -> displayCall()
            else -> {}
        }
    }
}