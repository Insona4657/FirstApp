package com.example.applogin.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.LoginHeadingTextComponent
import com.example.applogin.components.LoginNormalTextComponent
import com.example.applogin.components.ResetPasswordTextFieldComponent
import com.example.applogin.components.ToLoginClickableTextComponent
import com.example.applogin.data.ResetPasswordViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler


@Composable
fun ResetPassword(resetPasswordViewModel: ResetPasswordViewModel = viewModel()) {
    var userEmail by remember { mutableStateOf("") }
    var isResetInProgress by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            LoginNormalTextComponent(introText = "Forgot your Password?")
            Spacer(modifier = Modifier.height(5.dp))
            LoginHeadingTextComponent(introText = "Enter Email to Reset")
            Spacer(modifier = Modifier.height(5.dp))

            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
            ResetPasswordTextFieldComponent(
                labelValue = stringResource(R.string.email),
                imageVector = Icons.Default.Email,
                onTextSelected = {
                    userEmail = it
                },
            )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ToLoginClickableTextComponent() {
                AppRouter.navigateTo(Screen.LoginScreen)
            }
            Spacer(modifier = Modifier.height(120.dp))
            Button(
                onClick = {
                    if (userEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        // Disable button and start reset process
                        isResetInProgress = true
                        resetPasswordViewModel.resetPassword(
                            userEmail = userEmail,
                            onResetCompleted = {
                                // Password reset email sent successfully
                                showToast = true
                                toastMessage = "Password reset email sent successfully."
                            },
                            onError = { errorMessage ->
                                // If there is an error during password reset
                                showToast = true
                                toastMessage = errorMessage
                            }
                        )
                    } else {
                        // Show error toast for invalid email
                        showToast = true
                        toastMessage = "Invalid email address."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 80.dp, end = 80.dp)
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(50.dp),
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .background(
                        color = Color(255, 165, 0),
                        shape = RoundedCornerShape(50.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Reset Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
            }
            // Show toast message if needed
            DisposableEffect(showToast) {
                if (showToast) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    // Reset state after showing the toast
                    showToast = false
                    isResetInProgress = false
                }

                onDispose { /* cleanup */ }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.LoginScreen)
    }
}

@Preview
@Composable
fun DefaultPreviewOfResetPassword() {
    ResetPassword()
}