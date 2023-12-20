package com.example.applogin

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.applogin.data.NotificationModel
import com.google.common.reflect.TypeToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyFirebaseMessagingService : FirebaseMessagingService() {
    // Get the existing NotificationViewModel from the ViewModelProvider

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Title: ${it.title}")
        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
        Log.d(TAG, "Before saveNotificationLocally")
        // Save notification locally
        saveNotificationLocally(applicationContext, remoteMessage)
        Log.d(TAG, "After saveNotificationLocally")

        // Get the updated notifications
        val notifications = getSavedNotifications(applicationContext)

        // Post the new notifications to the LiveData
        notificationLiveData.postValue(notifications)
    }
    companion object {
        fun saveNotificationLocally(context: Context, remoteMessage: RemoteMessage) {
            val title = remoteMessage.notification?.title ?: ""
            val body = remoteMessage.notification?.body ?: ""
            val timestamp = System.currentTimeMillis()

            // Convert timestamp to DDMMYY format
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val formattedTimestamp = dateFormat.format(Date(timestamp))

            // Save notification data to local storage (e.g., SharedPreferences, SQLite, etc.)
            // You can use a database to store notifications for a persistent solution
            val sharedPreferences = context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Read existing notifications
            val existingNotificationsJson = sharedPreferences.getString("notifications", "[]")
            val existingNotifications = Gson().fromJson(existingNotificationsJson, object : TypeToken<List<NotificationModel>>() {}.type) as MutableList<NotificationModel>

            // Add the new notification
            existingNotifications.add(NotificationModel(title, body, formattedTimestamp, false))

            // Save the updated list
            val updatedNotificationsJson = Gson().toJson(existingNotifications)
            editor.putString("notifications", updatedNotificationsJson)
            editor.apply()
            // Log to verify the saving process
            Log.d(TAG, "Saved notification locally: $title, $body, $formattedTimestamp")

        }
        fun getSavedNotifications(context: Context): List<NotificationModel> {
            val sharedPreferences = context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val notificationsJson = sharedPreferences.getString("notifications", "[]")
            return Gson().fromJson(notificationsJson, object : TypeToken<List<NotificationModel>>() {}.type) as List<NotificationModel>
        }

        val notificationLiveData: MutableLiveData<List<NotificationModel>> = MutableLiveData<List<NotificationModel>>()
        // Add a function to mark a notification as read
        // Add a function to mark a notification as read using timestamp
        fun markNotificationAsRead(context: Context, timestamp: String) {
            val sharedPreferences =
                context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Read existing notifications
            val existingNotificationsJson =
                sharedPreferences.getString("notifications", "[]")
            val existingNotifications =
                Gson().fromJson(
                    existingNotificationsJson,
                    object : TypeToken<List<NotificationModel>>() {}.type
                ) as MutableList<NotificationModel>

            // Find the notification to mark as read based on timestamp
            val notificationToMarkAsRead =
                existingNotifications.firstOrNull { it.timestamp == timestamp }
            notificationToMarkAsRead?.let {
                it.read = true
            }
        }
    }
}