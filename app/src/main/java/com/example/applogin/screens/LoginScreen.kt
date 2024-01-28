package com.example.applogin.screens

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.ClickableForgetPasswordComponent
import com.example.applogin.components.ClickableRegistrationPage
import com.example.applogin.components.LoginButton
import com.example.applogin.components.LoginHeadingTextComponent
import com.example.applogin.components.LoginMyTextFieldComponent
import com.example.applogin.components.LoginNormalTextComponent
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.data.login.LoginUIEvent
import com.example.applogin.data.login.LoginViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.AppRouter.navigateTo
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), context: Context = LocalContext.current, homeViewModel: HomeViewModel = viewModel()) {
    var isDialogVisible by remember { mutableStateOf(false) }
    // Observe loginFailed and loginInProgress
    val loginFailed by loginViewModel.loginFailed.observeAsState(false)
    val loginInProgress by loginViewModel.loginInProgress.observeAsState(false)
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.syndes_bg_login_page),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            focusManager.clearFocus()
                        }
                    }
            ) {
                // Show notification permission dialog if not already granted
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp
                        )
                        .size(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                }
                Spacer(modifier = Modifier.height(20.dp))
                LoginNormalTextComponent(introText = "Hello,")
                Spacer(modifier = Modifier.height(5.dp))
                LoginHeadingTextComponent(introText = "Welcome !")
                Spacer(modifier = Modifier.height(5.dp))

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    LoginMyTextFieldComponent(
                        labelValue = stringResource(R.string.email),
                        imageVector = Icons.Default.Email,
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError,
                    )
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        imageVector = Icons.Default.Lock,
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                ClickableForgetPasswordComponent() {
                    navigateTo(Screen.ResetPasswordScreen)
                }
                /* Navigation to Registration Page for initial setup
                Spacer(modifier = Modifier.height(10.dp))
                ClickableRegistrationPage(onTextSelected = {
                    AppRouter.navigateTo(Screen.SignUpScreen)
                })
                 */
                Spacer(modifier = Modifier.height(80.dp))

                LoginButton(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        if (!loginViewModel.allValidationsPassed.value) {
                            // Show toast message for empty or incorrect email/password
                            Toast.makeText(context, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
                        }
                        // Check if email and password are correct
                        if (loginInProgress) {
                            // Show AlertDialog for incorrect email or password
                            isDialogVisible = true
                        } else {
                            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        }
                    }
                )
                if(loginViewModel.loginSuccess.value == true){
                    UpdateUserDetailsLogin(true, homeViewModel)
                    navigateTo(Screen.HomeScreen)
                }



                // Previous link to link to registration page
                /*
                DividerTextComponent()
                Spacer(modifier = Modifier.height(20.dp))
                ToRegistrationTextComponent() {
                    AppRouter.navigateTo(Screen.SignUpScreen)
                }
                */
            }
        // AlertDialog for incorrect email or password
        if (loginFailed) {
            AlertDialog(
                onDismissRequest = {
                    // Reset loginFailed state when dialog is dismissed
                    loginViewModel.setLoginFailed(false)
                    loginViewModel.setLoginInProgress(false)
                },
                title = { Text("Incorrect Email or Password") },
                text = { Text("Please check your email and password and try again.") },
                confirmButton = {
                    TextButton(onClick = {
                        // Reset loginFailed state when OK button is clicked
                        loginViewModel.setLoginFailed(false)
                        loginViewModel.setLoginInProgress(false)
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }

    // CircularProgressIndicator
    if (loginInProgress && !loginFailed) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }

    SystemBackButtonHandler {
        navigateTo(Screen.LoginScreen)
    }
}
@Composable
fun UpdateUserDetailsLogin(successfulLogin: Boolean, homeViewModel: HomeViewModel) {
    if(successfulLogin){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.email?.let { homeViewModel.updateUserEmail(it) }
            homeViewModel.checkStatus()
            if (homeViewModel.isAdminCheckExecuted == false) {
                homeViewModel.isAdminUser()
                homeViewModel.isAdminCheckExecuted = true
            }
        }
        Log.d(ContentValues.TAG, "Update User details status")
    }
}
@Preview
@Composable
fun DefaultPreviewOfLoginScreen() {
    LoginScreen()
}