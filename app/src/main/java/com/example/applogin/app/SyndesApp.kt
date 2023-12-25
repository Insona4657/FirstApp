package com.example.applogin.app

import android.provider.Telephony.Mms.Inbox
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
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

@Composable
fun SyndesApp(homeViewModel: HomeViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        if (homeViewModel.isUserLoggedIn.value == true){
            //Change Screen Here for Testing/Development
            //Sets the Navigation Screen after login
            AppRouter.navigateTo(Screen.InboxScreen)
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
                    TransitionScreen()
                }
                is Screen.HomeScreen -> {
                    TransitionScreen()
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
            }
        }
    }
}


