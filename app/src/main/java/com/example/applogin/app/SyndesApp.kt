package com.example.applogin.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.screens.WarrantyScreen
import com.example.applogin.screens.LoginScreen
import com.example.applogin.screens.SignUpScreen
import com.example.applogin.screens.TermsAndConditionsScreen
import com.example.applogin.screens.TransitionScreen

@Composable
fun SyndesApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Crossfade(targetState = AppRouter.currentScreen) { currentState ->
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
            }
        }
    }
}