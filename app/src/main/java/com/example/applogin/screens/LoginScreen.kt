package com.example.applogin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.ClickableForgetPasswordComponent
import com.example.applogin.components.DividerTextComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.components.ToRegistrationTextComponent
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler

@Composable
fun LoginScreen() {
    Surface(
        modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            NormalTextComponent(introText = stringResource(R.string.login))
            HeadingTextComponent(introText = stringResource(R.string.welcome))
            MyTextFieldComponent(
                labelValue = stringResource(R.string.email),
                imageVector = Icons.Default.Email,
                onTextSelected = {

                })
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                imageVector = Icons.Default.Lock,
                onTextSelected = {

                })
            Spacer(modifier = Modifier.height(7.dp))
            ClickableForgetPasswordComponent() {

            }
            Spacer(modifier = Modifier.height(120.dp))
            ButtonComponent(
                value = stringResource(id = R.string.login),
                onButtonClicked = {

            },
                isEnabled = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(20.dp))
            ToRegistrationTextComponent() {
                AppRouter.navigateTo(Screen.SignUpScreen)
            }
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUpScreen)
    }
}


@Preview
@Composable
fun DefaultPreviewOfLoginScreen() {
    LoginScreen()
}