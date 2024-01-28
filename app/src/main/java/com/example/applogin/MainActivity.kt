package com.example.applogin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.applogin.MyFirebaseMessagingService.Companion.handleDataPayload
import com.example.applogin.app.SyndesApp
import com.example.applogin.data.home.HomeViewModel
import com.example.compose.AppTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply{
            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }
        }
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    SyndesApp(){
                        moveTaskToBack(true)
                    }
                }
            }

        }
        //Firebase Messaging Handling to check if registration token failed to receive and if it is received to print it out
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "Token: $token")
        })

        // If app is in the background when message is sent, it triggers intent and saves the key value pairs to be viewed later on.
        var intent = intent
        Log.d(TAG, "INSIDE INTENT")
        // Check if the intent has extras
        if (intent != null && intent.extras != null) {
            // Retrieve the extras from the intent
            var extras = intent.extras

            // Log the values in extras
            for (key in extras!!.keySet()) {
                val value = extras.getString(key)
                Log.d(TAG, "Extra: $key -> $value")
            }

            // Convert Bundle to Map<String, String>
            var extrasMap = bundleToMap(extras)

            // Log the values in extrasMap
            for ((key, value) in extrasMap) {
                Log.d(TAG, "ExtrasMap: $key -> $value")
            }

            // Pass the extras to handleDataPayload
            handleDataPayload(applicationContext, extrasMap)
            Log.d(TAG, "HANDLE DATA PAYLOAD TRIGGERED")
        }
    }
    // Extra function to convert bundle into key value pairs to be input into handleDataPayload Function
    private fun bundleToMap(extras: Bundle?): Map<String, String> {
        val result = mutableMapOf<String, String>()

        extras?.let {
            for (key in it.keySet()) {
                val value = it.getString(key) ?: ""
                result[key] = value
            }
        }
        return result
    }
    /*
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

     */
}
