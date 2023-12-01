package com.example.applogin.loginflow.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen{
    object SignUpScreen : Screen()
    object TermsAndConditionsScreen : Screen()
    object LoginScreen : Screen()
    object HomeScreen : Screen()
    object Transition : Screen()
    object WarrantySearch: Screen()
}

object AppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.Transition)

    fun navigateTo(destination : Screen) {
        currentScreen.value = destination
    }
    fun getScreenForTitle(title: String): Screen {
        return when (title) {
            "SignUpScreen" -> Screen.SignUpScreen
            "TermsAndConditionsScreen" -> Screen.TermsAndConditionsScreen
            "LoginScreen" -> Screen.LoginScreen
            "HomeScreen" -> Screen.HomeScreen
            "Transition" -> Screen.Transition
            "WarrantySearch" -> Screen.WarrantySearch
            // Add more cases as needed for other screens
            else -> Screen.Transition
        }
    }
}