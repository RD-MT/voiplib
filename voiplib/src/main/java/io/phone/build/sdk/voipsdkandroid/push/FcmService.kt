package io.phone.build.sdk.voipsdkandroid.push

//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import io.phone.build.sdk.voipsdkandroid.PIL
import io.phone.build.sdk.voipsdkandroid.di.di
import io.phone.build.sdk.voipsdkandroid.isNullOrInvalid
import io.phone.build.sdk.voipsdkandroid.logWithContext
import io.phone.build.sdk.voipsdkandroid.push.UnavailableReason.*
import io.phone.build.sdk.voipsdkandroid.telecom.AndroidCallFramework

class FcmService {}

/*internal class FcmService : FirebaseMessagingService() {

    private val pil: PIL by lazy { di.koin.get() }
    private val androidCallFramework: AndroidCallFramework by lazy { di.koin.get() }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (!PIL.isInitialized) return

        if (pil.auth.isNullOrInvalid) {
            log("Ignoring notification as there is no auth.")
            return
        }

        val middleware = pil.app.middleware ?: return

        if (!middleware.inspect(remoteMessage)) {
            log("Client has inspected push message and determined this is not a call")
            return
        }

        log("Received FCM push message")

        if (pil.calls.isInCall) {
            log("Currently in Vialer call, rejecting incoming call")
            middleware.respond(remoteMessage, false, IN_CALL)
            return
        }

        if (!androidCallFramework.canHandleIncomingCall) {
            log("The android call framework cannot handle incoming call, responding as unavailable")
            middleware.respond(remoteMessage, false, REJECTED_BY_ANDROID_TELECOM_FRAMEWORK)
            return
        }

        // When booted from cold we often don't have network connectivity immediately,
        // this can cause issues. So if we detect this situation we will just continually wait
        // for the network to come back.
        whenNetworkReachable(remoteMessage, middleware) {
            log("Continuing to register")

            pil.phoneLibHelper.register { success ->
                when (success) {
                    true -> middleware.respond(remoteMessage, true)
                    false -> middleware.respond(remoteMessage, false, UNABLE_TO_REGISTER)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (!PIL.isInitialized) return

        log("Received new FCM token")

        pil.app.middleware?.tokenReceived(token)
    }

    *//**
     * Wait for connectivity by continuously checking if the network is reachable in a loop.
     *//*
    private fun whenNetworkReachable(
        remoteMessage: RemoteMessage,
        middleware: Middleware,
        block: () -> Unit,
    ) {
        if (isNetworkReachable) {
            log("Executing immediately as the network is reachable")
            block()
            return
        }

        log("The network is currently not reachable, waiting for up to ${AWAIT_CONNECTIVITY_TIME}ms")

        val endTime = currentTime + AWAIT_CONNECTIVITY_TIME

        MainScope().launch {
            while(isNetworkReachable) {
                if (currentTime > endTime) {
                    log("We aren't getting connectivity, abandoning call")
                    middleware.respond(remoteMessage, false, NO_CONNECTIVITY)
                    return@launch
                }

               delay(AWAIT_CONNECTIVITY_INTERVAL)
            }

            log("We now have connectivity, resuming incoming call flow")
            block()
        }
    }

    private fun log(message: String) = logWithContext(message, "FCM-SERVICE")

    private val currentTime
        get() = System.currentTimeMillis()

    private val isNetworkReachable
        get() = pil.voipLib.isNetworkReachable

    companion object {
        *//**
         * The interval between connectivity checks.
         *//*
        const val AWAIT_CONNECTIVITY_INTERVAL = 50L

        *//**
         * The number of milliseconds that we will wait for connectivity before abandoning it.
         *//*
        const val AWAIT_CONNECTIVITY_TIME = 1000L
    }
}*/

enum class UnavailableReason {
    IN_CALL,
    REJECTED_BY_ANDROID_TELECOM_FRAMEWORK,
    UNABLE_TO_REGISTER,
    NO_CONNECTIVITY,
}
