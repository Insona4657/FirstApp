package com.example.applogin.data.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RequestPage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.NavigationItem
import com.example.applogin.data.NotificationModel
import com.example.applogin.data.UserData
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.switchMap

class HomeViewModel(): ViewModel() {
    private val TAG = HomeViewModel::class.simpleName

    val isUserAdmin : MutableLiveData<Boolean> = MutableLiveData()

    init {
        // Observe changes to isUserAdmin and update the navigation list accordingly
        isUserAdmin.observeForever {
            updateNavigationList()
        }
    }
    var navigationItemsList = listOf<NavigationItem>(
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
    fun updateNavigationList() {
        val isAdmin = isUserAdmin.value ?: false
        if (isAdmin) {
            val adminItem = NavigationItem(
                title = "Home",
                icon = Icons.Default.PersonAdd,
                description = "Registration",
                itemId = "signupScreen"
            )
            navigationItemsList = navigationItemsList + adminItem
            Log.d(TAG, "isAdmin is true. Added adminItem to navigationItemsList.")
        } else {
            Log.d(TAG, "isAdmin is false. No additional item added to navigationItemsList.")
        }
    }
    fun isAdminUser() {
        Log.d(TAG, "Starting isAdminUser Check")

        val firestore = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        val usersCollection = firestore.collection("users")

        if (currentUser != null) {
            Log.d(TAG, "CurrentUser ID: $currentUser")

            usersCollection.document(currentUser).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "DocumentSnapshot exists")

                        val userData = documentSnapshot.toObject(UserData::class.java)
                        if (userData != null) {
                            Log.d(TAG, "User Data fetched successfully")

                            // Access user data and check if the user is an admin
                            if (userData.status == "Admin") {
                                Log.d(TAG, "User is an Admin")
                                isUserAdmin.value = true
                                Log.d(TAG, "isUserAdmin.value is set to true: ${isUserAdmin.value}")
                            } else {
                                Log.d(TAG, "User is not an Admin")
                                isUserAdmin.value = false
                            }
                        } else {
                            // Handle the case where documentSnapshot exists but userData is null
                            Log.d(TAG, "User Data is null")
                            isUserAdmin.value = false
                        }
                    } else {
                        // Handle the case where documentSnapshot doesn't exist
                        Log.d(TAG, "DocumentSnapshot does not exist")
                        isUserAdmin.value = false
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure to fetch data
                    Log.e(TAG, "Error fetching user data: ${e.message}")
                    isUserAdmin.value = false
                }
        } else {
            // No user is signed in
            Log.d(TAG, "No user is signed in")
            isUserAdmin.value = false
        }

        Log.d(TAG, "isAdminUser Check completed")
    }


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

