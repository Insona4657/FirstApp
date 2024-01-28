package com.example.applogin.app

import android.content.ContentValues.TAG
import android.provider.Telephony.Mms.Inbox
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.data.login.LoginViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler
import com.example.applogin.screens.BarcodeScannerScreen
import com.example.applogin.screens.InboxScreen
import com.example.applogin.screens.WarrantyScreen
import com.example.applogin.screens.LoginScreen
import com.example.applogin.screens.MyProductScreen
import com.example.applogin.screens.ProductScreen
import com.example.applogin.screens.ResetPassword
import com.example.applogin.screens.ServiceRequestScreen
import com.example.applogin.screens.SignUpScreen
import com.example.applogin.screens.TermsAndConditionsScreen
import com.example.applogin.screens.TransitionScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SyndesApp(homeViewModel: HomeViewModel = viewModel(),onBackPressed: () -> Unit) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        if (homeViewModel.isUserLoggedIn.value == true){
            //Change Screen Here for Testing/Development
            //Sets the Navigation Screen after login
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                user.email?.let { homeViewModel.updateUserEmail(it) }
                homeViewModel.checkStatus()
            }
            Log.d(TAG, "INSIDE SYNDES APP IMPLEMENTATION")
            AppRouter.navigateTo(Screen.Transition)
        }

        Crossfade(targetState = AppRouter.currentScreen, label = "") { currentState ->
            when(currentState.value) {
                is Screen.WarrantySearch -> {
                    WarrantyScreen()
                }
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }
                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.Transition -> {
                    TransitionScreen(
                        onBackPressed = onBackPressed
                    )
                }
                is Screen.HomeScreen -> {
                    TransitionScreen(
                        onBackPressed = onBackPressed
                    )
                }
                is Screen.ProductPage -> {
                    ProductScreen()
                }
                is Screen.Service -> {
                    ServiceRequestScreen()
                }
                is Screen.MyProduct -> {
                    MyProductScreen()
                }
                is Screen.BarcodeScanner -> {
                    BarcodeScannerScreen()
                }
                is Screen.InboxScreen -> {
                    InboxScreen()
                }
                is Screen.ResetPasswordScreen -> {
                    ResetPassword()
                }

                else -> {}
            }
        }
    }
}


