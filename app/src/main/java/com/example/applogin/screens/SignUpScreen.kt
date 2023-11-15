package com.example.applogin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.CheckboxComponent
import com.example.applogin.components.ClickableLoginTextComponent
import com.example.applogin.components.ClickableTextComponent
import com.example.applogin.components.DividerTextComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.data.LoginViewModel
import com.example.applogin.data.UIEvent
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler

@Composable
fun SignUpScreen(loginViewModel : LoginViewModel = viewModel()) {

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(introText = stringResource(id = R.string.hello))
            HeadingTextComponent(introText = stringResource(id = R.string.create_account))
            Spacer(modifier = Modifier.height(20.dp))
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.first_name),
                imageVector = Icons.Default.Person,
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.FirstNameChanged(it))
                })
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.last_name),
                imageVector = Icons.Default.Person,
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.LastNameChanged(it))
                })
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                imageVector = Icons.Default.Email,
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.EmailChanged(it))
                })
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                imageVector = Icons.Default.Lock,
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.PasswordChanged(it))
                })
            CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                onTextSelected = {
                    AppRouter.navigateTo(Screen.TermsAndConditionsScreen)
            })
            Spacer(modifier = Modifier.height(60.dp))
            ButtonComponent(
                value = stringResource(id = R.string.register),
                onButtonClicked = {

            },
                isEnabled = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(20.dp))
            ClickableLoginTextComponent(onTextSelected = {
                AppRouter.navigateTo(Screen.LoginScreen)
            })
            }
        }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.LoginScreen)
    }
    }



@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    SignUpScreen()
}