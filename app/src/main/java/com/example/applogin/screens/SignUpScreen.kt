package com.example.applogin.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.ClickableBackToHomeScreen
import com.example.applogin.components.ClickableForgetPasswordComponent
import com.example.applogin.components.ClickableLoginTextComponent
import com.example.applogin.components.DropdownTextFieldComponent
import com.example.applogin.components.LoginHeadingTextComponent
import com.example.applogin.components.LoginMyTextFieldComponent
import com.example.applogin.components.LoginNormalTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.data.signupregistration.SignupViewModel
import com.example.applogin.data.signupregistration.SignupUIEvent
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler
import com.example.applogin.ui.theme.Shapes

@Composable
fun SignUpScreen(signupViewModel : SignupViewModel = viewModel())
{
    var selectedChoice by remember { mutableStateOf("")}
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        //Background Image
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    focusManager.clearFocus()
                }
            }
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Column(modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp)
            ) {
                //LoginNormalTextComponent(introText = stringResource(id = R.string.hello))
                Spacer(modifier = Modifier.height(5.dp))
                LoginHeadingTextComponent(introText = "Registration Page")
                Spacer(modifier = Modifier.height(20.dp))
                LoginMyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    imageVector = Icons.Default.Person,
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
                )

                LoginMyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    imageVector = Icons.Default.Person,
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.lastNameError
                )
                LoginMyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    imageVector = Icons.Default.Email,
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.emailError
                )

                DropdownTextFieldComponent(
                    labelValue = "Company Name",
                    imageVector = Icons.Default.AccountCircle,
                    onTextSelected = {
                        expanded = true
                    },
                    signUpViewModel = signupViewModel,
                    onOptionSelected = { onOptionSelected ->
                        signupViewModel.onEvent(SignupUIEvent.CompanyNameChanged(onOptionSelected))
                    }
                )
                Box(modifier = Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    selectUserStatus(
                        labelValue = "Enter User Status 'User' / 'Admin' ",
                        imageVector = Icons.Default.ListAlt,
                        onTextSelected = {
                            signupViewModel.onEvent(SignupUIEvent.UserStatusChanged(it))
                        },
                        signupViewModel = signupViewModel
                    )
                }
                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    imageVector = Icons.Default.Lock,
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.passwordError
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
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
                    Text(text = "Register",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            /* Navigation to Login Screen But it is not needed for Admins
            ClickableLoginTextComponent(onTextSelected = {
                AppRouter.navigateTo(Screen.LoginScreen)
            })
             */
            ClickableBackToHomeScreen(onTextSelected = {
                AppRouter.navigateTo(Screen.Transition)
            })

        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.HomeScreen)
    }
    if(signupViewModel.signUpInProgress.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun selectUserStatus(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean=false, signupViewModel: SignupViewModel){
    var textValue = remember {
        mutableStateOf("")
    }
    var expanded by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    // Wrap the OutlinedTextField with Clickable
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small)
            .onFocusChanged {
                focused = it.isFocused
                expanded = focused
            },
        label = {
            Text(
                text = labelValue,
                color = Color.White
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        },
    )
    AnimatedVisibility(visible = expanded) {
        // Access the value of filteredCompanyNames
        val options = listOf("User", "Admin")
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        textValue.value = option
                        expanded = !expanded
                        onTextSelected(option)
                    },
                    text = {
                        Text(option)
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    SignUpScreen()
}