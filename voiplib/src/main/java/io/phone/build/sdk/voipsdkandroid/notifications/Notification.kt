package io.phone.build.sdk.voipsdkandroid.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.phone.build.sdk.voipsdkandroid.PIL
import io.phone.build.sdk.voipsdkandroid.di.di
import io.phone.build.sdk.voipsdkandroid.service.NotificationButtonReceiver

internal abstract class Notification {

    protected val pil: PIL by di.koin.inject()
    protected val context: Context by di.koin.inject()
    protected val notificationManger: NotificationManager by di.koin.inject()

    protected abstract val channelId: String
    abstract val notificationId: Int

    abstract fun createNotificationChannel()

    open fun cancel() {
        notificationManger.cancel(notificationId)
    }

    protected fun createActionIntent(action: NotificationButtonReceiver.Action, context: Context): PendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, NotificationButtonReceiver::class.java).apply {
            setAction(action.name)
        },
        PendingIntent.FLAG_IMMUTABLE
    )
}