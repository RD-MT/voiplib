package io.phone.build.sdk.voiptestexample

import android.app.Application
import android.util.Log
import io.phone.build.sdk.voiptestexample.ui.call.IncomingCallActivity
import io.phone.build.sdk.voiptestexample.ui.call.CallActivity
import io.phone.build.sdk.voipsdkandroid.configuration.ApplicationSetup
import io.phone.build.sdk.voipsdkandroid.startAndroidPIL

class VoipExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startAndroidPIL {

            ApplicationSetup(
                application = this@VoipExampleApplication,
                activities = ApplicationSetup.Activities(
                    call = CallActivity::class.java,
                    incomingCall = IncomingCallActivity::class.java
                ),
                middleware = null,
                logger = { message, _ -> Log.i("VoipLib-Logger", message) },
                userAgent = "Android SDK Kotlin 1.8.0",
                notifyOnMissedCall = true,
                onMissedCallNotificationPressed = null
            )
        }
    }
}