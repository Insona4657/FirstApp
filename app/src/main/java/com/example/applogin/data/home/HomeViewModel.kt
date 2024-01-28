package com.example.applogin.data.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RequestPage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.NavigationItem
import com.example.applogin.data.UserData
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel(){
    private val TAG = HomeViewModel::class.simpleName
    val isUserAdmin : MutableLiveData<Boolean> = MutableLiveData()
    var isAdminCheckExecuted = false
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()
    private lateinit var firestore : FirebaseFirestore
    var hasUserChangedEmail: MutableLiveData<Boolean> = MutableLiveData()
    var hasUserChangedPW : MutableLiveData<Boolean> = MutableLiveData()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail
    init {
        // Observe changes to isUserAdmin and update the navigation list accordingly
        isUserAdmin.observeForever {
            updateNavigationList()
        }
        viewModelScope.launch{
            delay(2000L)
            _isReady.value=true
        }
    }

    var navigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            description = "Home Screen",
            itemId = "homeScreen"),
        NavigationItem(
            title = "Warranty",
            icon = Icons.Default.ManageSearch,
            description = "Warranty Search",
            itemId = "warrantySearch"),
        NavigationItem(
            title = "Products",
            icon = Icons.Default.ProductionQuantityLimits,
            description = "Products",
            itemId = "products"),
        NavigationItem(
            title = "Inbox",
            icon = Icons.Default.Email,
            description = "Inbox Page",
            itemId = "inboxPage"),
        NavigationItem(
            title = "Service Form",
            icon = Icons.Default.RequestPage,
            description = "Service Request",
            itemId = "serviceRequest"),
        NavigationItem(
            title = "Barcode Scanner",
            icon = Icons.Default.QrCodeScanner,
            description = "Barcode Scanner",
            itemId = "barcodeScanner"),
    )
    var adminnavigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            description = "Home Screen",
            itemId = "homeScreen"),
        NavigationItem(
            title = "Warranty",
            icon = Icons.Default.ManageSearch,
            description = "Warranty Search",
            itemId = "warrantySearch"),
        NavigationItem(
            title = "Products",
            icon = Icons.Default.ProductionQuantityLimits,
            description = "Products",
            itemId = "products"),
        NavigationItem(
            title = "Inbox",
            icon = Icons.Default.Email,
            description = "Inbox Page",
            itemId = "inboxPage"),
        NavigationItem(
            title = "Service Form",
            icon = Icons.Default.RequestPage,
            description = "Service Request",
            itemId = "serviceRequest"),
        NavigationItem(
            title = "Barcode Scanner",
            icon = Icons.Default.QrCodeScanner,
            description = "Barcode Scanner",
            itemId = "barcodeScanner"),
        NavigationItem(
            title = "Sign Up",
            icon = Icons.Default.PersonAdd,
            description = "Sign Up",
            itemId = "signUp"),
    )

    // Function to update the user's email
    fun updateUserEmail(newEmail: String) {
        _userEmail.value = newEmail
    }
    // Function to update the user status
    fun updateUserStatus() {
        hasUserChangedEmail.value = true
    }
    fun updateUserPWStatus() {
        hasUserChangedPW.value = true
    }
    fun resetPassword(
        userEmail: String,
        onResetCompleted: () -> Unit,
        onError: (String) -> Unit,
        context : Context
    ) {
        if (userEmail.isBlank()) {
            val errorMessage = "Email cannot be empty"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            onError(errorMessage)
            return
        }

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != currentUserEmail){
            val errorMessage = "Email does not Match account Email"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            onError(errorMessage)
            return
        }

        auth.sendPasswordResetEmail(userEmail)
            .addOnSuccessListener {
                // Password reset email sent successfully
                onResetCompleted.invoke()
                val user = FirebaseAuth.getInstance().currentUser
                val userDocRef = FirebaseFirestore.getInstance().collection("users").document(user!!.uid)
                userDocRef.update("changed_pw", true)
            }
            .addOnFailureListener { e ->
                // If there is an error during password reset
                onError.invoke(e.message ?: "Password reset failed.")
            }
    }
    fun checkStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(user!!.uid)
        userDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val changedEmail = documentSnapshot.getBoolean("changed_email") ?: false
                val changedPW = documentSnapshot.getBoolean("changed_pw") ?: false
                hasUserChangedPW.value = changedPW
                hasUserChangedEmail.value = changedEmail
                Log.d("User Changed Email", hasUserChangedEmail.value.toString())
                Log.d("User Changed PW", hasUserChangedPW.value.toString())
            }
        }
        user.email?.let { updateUserEmail(it) }
    }
    fun updateNavigationList() {
        val isAdmin = isUserAdmin.value ?: false
        if (isAdmin) {
            val adminItem = NavigationItem(
                title = "Sign Up",
                icon = Icons.Default.PersonAdd,
                description = "Sign Up",
                itemId = "Sign Up"
            )
            //navigationItemsList = navigationItemsList + adminItem
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
                hasUserChangedPW.value = false
                hasUserChangedEmail.value = false
                isUserAdmin.value = false
                updateNavigationList()
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

