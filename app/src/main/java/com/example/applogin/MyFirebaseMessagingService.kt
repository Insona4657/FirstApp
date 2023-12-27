package com.example.applogin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
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
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message received: ${remoteMessage.data}")
        Log.d(TAG, "Notification: ${remoteMessage.notification}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            // Handle the data and perform any necessary actions
            remoteMessage.data?.let { data ->
                for ((key, value) in data) {
                    Log.d(TAG, "Data item: $key -> $value")
                }
                handleDataForeground(applicationContext, data)
            }
        } else {
            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Title: ${it.title}")
                Log.d(TAG, "Message Notification Body: ${it.body}")
            }

            Log.d(TAG, "Before saveNotificationLocally")

            // Save notification locally
            saveNotificationLocally(applicationContext, remoteMessage)

            Log.d(TAG, "After saveNotificationLocally")
        }

        // Get the updated notifications
        val notifications = getSavedNotifications(applicationContext)

        // Post the new notifications to the LiveData
        notificationLiveData.postValue(notifications)

        Log.d(TAG, "Before SUPER")
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "After SUPER")
    }

    companion object {
        fun handleDataPayload(context: Context, data: Map<String, String>) {
            val timestamp = System.currentTimeMillis()
            /*
            // Specify the indices (0-based) of the key-value pairs you want to extract
            val titleIndex = 6
            val bodyIndex = 4

            // Extract values based on indices
            var title = data.values.elementAtOrNull(titleIndex) ?: ""
            var body = data.values.elementAtOrNull(bodyIndex) ?: ""
             */

            // Specify the keys for title and body in your data map
            val titleKey = "title"
            val bodyKey = "message"

            // Extract values based on keys
            val title = data[titleKey] ?: ""
            val body = data[bodyKey] ?: ""

            // Log the values stored in title and body
            Log.d(TAG, "Title: $title, Body: $body")
            // Convert timestamp to DDMMYY format
            val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
            val formattedTimestamp = dateFormat.format(Date(timestamp))

            // Save data to local storage (SharedPreferences)
            saveDataLocally(context, title, body, formattedTimestamp, false)

            // Log to verify the saving process
            Log.d(TAG, "Saved data locally on background: $title, $body, $formattedTimestamp, false")
        }

        fun handleDataForeground(context: Context, data: Map<String, String>) {
            val timestamp = System.currentTimeMillis()

            // Specify the indices (0-based) of the key-value pairs you want to extract
            val titleIndex = 1
            val bodyIndex = 0

            // Extract values based on indices
            var title = data.values.elementAtOrNull(titleIndex) ?: ""
            var body = data.values.elementAtOrNull(bodyIndex) ?: ""

            // Log the values stored in title and body
            Log.d(TAG, "Title: $title, Body: $body")
            // Convert timestamp to DDMMYY format
            val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
            val formattedTimestamp = dateFormat.format(Date(timestamp))

            // Save data to local storage (SharedPreferences)
            saveDataLocally(context, title, body, formattedTimestamp, false)

            // Log to verify the saving process
            Log.d(TAG, "Saved data on Foreground: $title, $body, $formattedTimestamp, false")
        }

        fun saveDataLocally(context: Context, title: String, body: String, formattedTimestamp: String, b: Boolean) {
            val sharedPreferences = context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Read existing data
            val existingDataJson = sharedPreferences.getString("notifications", "[]")
            val existingData = Gson().fromJson(existingDataJson, object : TypeToken<List<NotificationModel>>() {}.type) as MutableList<NotificationModel>

            // Add the new data with the default "read" flag set to false
            existingData.add(NotificationModel(title, body, formattedTimestamp, b))

            // Save the updated list
            val updatedDataJson = Gson().toJson(existingData)
            editor.putString("notifications", updatedDataJson)
            editor.apply()
        }
        fun saveNotificationLocally(context: Context, remoteMessage: RemoteMessage) {
            val title = remoteMessage.notification?.title ?: ""
            val body = remoteMessage.notification?.body ?: ""
            val timestamp = System.currentTimeMillis()

            // Convert timestamp to DDMMYY format
            val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
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
        fun markNotificationAsRead(context: Context, timestamp: String) {
            val sharedPreferences = context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Read existing notifications
            val existingNotificationsJson = sharedPreferences.getString("notifications", "[]")
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

            // Log to verify the marking as read process
            Log.d(TAG, "Marked notification as read: $timestamp")

            // Save the updated list
            val updatedNotificationsJson = Gson().toJson(existingNotifications)
            editor.putString("notifications", updatedNotificationsJson)
            editor.apply()

        }
        fun clearAllSharedPreferences(context: Context) {
            // Log to verify the clearing process
            Log.d(TAG, "Cleared all SharedPreferences data")

            val sharedPreferences = context.getSharedPreferences("MyNotifications", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            // Clear all key-value pairs in the SharedPreferences
            editor.clear()
            editor.apply()


        }
    }
}