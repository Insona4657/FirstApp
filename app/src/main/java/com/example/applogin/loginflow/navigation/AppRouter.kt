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
    object Service : Screen()
    object MyProduct : Screen()
    object BarcodeScanner : Screen()
    object InboxScreen : Screen()
    object ResetPasswordScreen : Screen()
}

object AppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.LoginScreen)
    fun navigateTo(destination : Screen) {
        currentScreen.value = destination
    }
    fun getScreenForTitle(title: String): Screen {
        return when (title) {
            "Home" -> Screen.HomeScreen
            "Warranty Search" -> Screen.WarrantySearch
            "Products Page" -> Screen.ProductPage
            "Service Request" -> Screen.Service
            "My Product" -> Screen.MyProduct
            "Barcode Scanner" -> Screen.BarcodeScanner
            "Inbox" -> Screen.InboxScreen
            // Add more cases as needed for other screens
            else -> Screen.Transition
        }
    }
    /*
    fun navigateProductDetail(destination : Screen, productModel :String){
        currentScreen.value =
    }
    */

}