package com.example.applogin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler

@Composable
fun TermsAndConditionsScreen() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
        .padding(16.dp)) {
        HeadingTextComponent(stringResource(id = R.string.terms_and_conditions_detail))
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUpScreen)
    }
}



@Preview
@Composable
fun TermsAndConditionsScreenPreview() {
    TermsAndConditionsScreen()
}