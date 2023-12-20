package com.example.applogin.data.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RequestPage
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.NavigationItem
import com.example.applogin.data.NotificationModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class HomeViewModel(): ViewModel() {
    private val TAG = HomeViewModel::class.simpleName

    val navigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            description = "Home Screen",
            itemId = "homeScreen"),
        NavigationItem(
            title = "Warranty Search",
            icon = Icons.Default.ManageSearch,
            description = "Warranty Search",
            itemId = "warrantySearch"),
        NavigationItem(
            title = "Products Page",
            icon = Icons.Default.ProductionQuantityLimits,
            description = "Products",
            itemId = "products"),
        NavigationItem(
            title = "Inbox",
            icon = Icons.Default.Email,
            description = "Inbox Page",
            itemId = "inboxPage"),
        NavigationItem(
            title = "Service Request",
            icon = Icons.Default.RequestPage,
            description = "Service Request",
            itemId = "serviceRequest"),
        NavigationItem(
            title = "Barcode Scanner",
            icon = Icons.Default.QrCodeScanner,
            description = "Barcode Scanner",
            itemId = "barcodeScanner"),
    )
    val isUserLoggedIn : MutableLiveData<Boolean> = MutableLiveData()
    fun logout() {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign out Success")
                AppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")

            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun checkForActiveSession(){
        if(FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid Session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }
}

