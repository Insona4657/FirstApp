package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.sharp.Email
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.ClickableForgetPasswordComponent
import com.example.applogin.components.DividerTextComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.LoginHeadingTextComponent
import com.example.applogin.components.LoginMyTextFieldComponent
import com.example.applogin.components.LoginNormalTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.components.ToRegistrationTextComponent
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.login.LoginUIEvent
import com.example.applogin.data.login.LoginViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.login_icon),
                        contentDescription = "Login Icon",
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                LoginNormalTextComponent(introText = "Hello,")
                Spacer(modifier = Modifier.height(5.dp))
                LoginHeadingTextComponent(introText = stringResource(R.string.welcome))
                Spacer(modifier = Modifier.height(5.dp))

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    LoginMyTextFieldComponent(
                        labelValue = stringResource(R.string.email),
                        imageVector = Icons.Default.Email,
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError
                    )
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        imageVector = Icons.Default.Lock,
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                ClickableForgetPasswordComponent() {

                }
                Spacer(modifier = Modifier.height(120.dp))
                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                /*
                DividerTextComponent()
                Spacer(modifier = Modifier.height(20.dp))
                ToRegistrationTextComponent() {
                    AppRouter.navigateTo(Screen.SignUpScreen)
                }
                */
            }
        }
    // CircularProgressIndicator
    if (loginViewModel.loginInProgress.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
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