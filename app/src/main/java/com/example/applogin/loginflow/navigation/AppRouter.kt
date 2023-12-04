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
    object ProductPage: Screen()
    object ProfilePage : Screen()
    object Service : Screen()
    object Contact : Screen()
}

object AppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.WarrantySearch)

    fun navigateTo(destination : Screen) {
        currentScreen.value = destination
    }
    fun getScreenForTitle(title: String): Screen {
        return when (title) {
            "Home" -> Screen.HomeScreen
            "Warranty Search" -> Screen.WarrantySearch
            "Products Page" -> Screen.ProductPage
            "Profile Page" -> Screen.ProfilePage
            "Service Request" -> Screen.Service
            "Contact" -> Screen.Contact

            // Add more cases as needed for other screens
            else -> Screen.Transition
        }
    }
}