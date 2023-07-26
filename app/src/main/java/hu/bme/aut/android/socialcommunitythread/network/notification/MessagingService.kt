package hu.bme.aut.android.socialcommunitythread.network.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hu.bme.aut.android.socialcommunitythread.MainActivity
import hu.bme.aut.android.socialcommunitythread.R
import java.util.*

@ExperimentalPermissionsApi
class MessagingService : FirebaseMessagingService() {

    private val POST_ID = "postId"
    private val THREAD_ID = "threadId"
    private val TITLE = "title"
    private val DATA = "data"
    private val CATEGORY = "category"
    private val PUSH_MESSAGE = "message"
    private val NOTIFICATION_CHANNEL_ID = "10002"
    private val NOTIFICATION_CHANNEL_NAME = "ASSISTRADE_NOTIFICATIONS"

    private var notificationManager: NotificationManager? = null
        get() {
            if (field == null) field = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return field
        }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(
            message.notification?.title.toString(),
            message.notification?.body.toString()
        )
        Log.d("PUSH_TOKEN", message.toString())
    }


    private fun showNotification(
        title: String,
        message: String
    ) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext).also {
            it
                .setSmallIcon(R.drawable.capybara)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle())
                //.setContentIntent(getPendingIntent(requestCode, pushDetailsModel))
                .setWhen(Date().time)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableLights(true)
                lightColor = Color.argb(9, 0, 0, 0)
                enableLights(true)
                shouldShowLights()
                enableVibration(true)
            }

            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)

            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val notificationId = createNotificationId()
        val notification = notificationBuilder.build()
        notificationManager!!.notify(notificationId, notification)
    }

    private fun createNotificationId() = (Math.random() * 100).toInt()

    override fun onNewToken(token: String) {
        //Save to preferences or not
        Log.d("PUSH_TOKEN", token)
        super.onNewToken(token)
    }
}