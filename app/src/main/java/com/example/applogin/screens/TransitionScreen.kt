package com.example.applogin.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.MyFirebaseMessagingService.Companion.countUnreadNotifications
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.PasswordTextFieldComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.NavigationIcon
import com.example.applogin.data.NotificationModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter.getScreenForTitle
import com.example.applogin.loginflow.navigation.AppRouter.navigateTo
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler
import com.example.applogin.ui.theme.Shapes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TransitionScreen(
    homeViewModel: HomeViewModel = viewModel(),
    context: Context = LocalContext.current
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val notificationPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    // Observe the LiveData using observeAsState
    val hasUserChangedEmail by homeViewModel.hasUserChangedEmail.observeAsState(initial = false)
    val hasUserChangedPW by homeViewModel.hasUserChangedPW.observeAsState(initial = false)

    var notifications by remember { mutableStateOf(MyFirebaseMessagingService.getSavedNotifications(context)) }
    //var email_list by remember { mutableStateOf() }
    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    NavigationDrawerHeader()
                    NavigationDrawerBody(
                        navigationDrawerItems = homeViewModel.navigationItemsList
                    ) {
                        Log.d(TAG, "Inside NavigationDrawer")
                        Log.d(TAG, "Inside ${it.itemId} ${it.title}")
                        navigateTo(getScreenForTitle(it.title))
                    }
                }
            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                mainAppBar(toolbarTitle = stringResource(R.string.home),
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    navigationIconClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    barcodeIconClicked = {
                        navigateTo(getScreenForTitle("Barcode Scanner"))
                    },
                    notificationIconClicked = {
                        navigateTo(getScreenForTitle("Inbox"))
                    },
                )
            },

            ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background,
            ) {
                MainPageTopBackground(
                    topimage = R.drawable.top_background,
                    middleimage = R.drawable.middle_background,
                    bottomimage = R.drawable.bottom_background
                )
                NotificationPermission(hasPermission = notificationPermissionState.status.isGranted,
                    onRequestPermission = {
                        notificationPermissionState.launchPermissionRequest()
                    })
                // Request notification permission
                CameraPermission(hasPermission = cameraPermissionState.status.isGranted,
                    onRequestPermission = {
                        cameraPermissionState.launchPermissionRequest()
                    })
                // Effect to trigger the checkAdminUser function on first composition
                val icons = listOf(
                    NavigationIcon("Warranty", R.drawable.warranty_logo),
                    NavigationIcon("Products", R.drawable.product_logo),
                    NavigationIcon("Inbox", R.drawable.inbox_logo),
                    NavigationIcon("Service Form", R.drawable.service_logo),
                )
                checkUserFirstLogin(homeViewModel, context, hasUserChangedEmail, onClose = {
                })
                checkUserChangedPW(homeViewModel, context, hasUserChangedEmail, hasUserChangedPW, onClose = {
                })
                TwoColumnNavigation(navIcons = icons)
            }
        }
    }
    SystemBackButtonHandler {
        navigateTo(Screen.Transition)
    }
}

@Composable
fun TwoColumnNavigation(navIcons: List<NavigationIcon>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
        items(navIcons.chunked(2)) { rowIcons ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.Transparent)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowIcons.forEach { icon ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25.dp))
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                            .background(Color.White, shape = RoundedCornerShape(25.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        NavigationIcon(icon)
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationIcon(icon: NavigationIcon) {
    navigationIcon(
        pageTitle = icon.navigationLocation,
        pageIcon = painterResource(id = icon.imageResId),
        navigationIconClicked = {
            Log.d(TAG, "Inside Page Navigation")
            Log.d(TAG, "Inside ${icon.navigationLocation}")
            navigateTo(getScreenForTitle(icon.navigationLocation))
        }
    )
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
) {
    if (hasPermission) {
        //No need to do anything
    } else {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            AlertDialog(
                onDismissRequest = {
                    onRequestPermission
                },
                title = { Text(text = "Notification Permission") },
                text = { Text(text = "Notification is required for the App Function") },
                confirmButton = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Button(onClick = onRequestPermission) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                            Text(text = "Grant Permission")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CameraPermission(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
) {
    if (hasPermission) {
        //No need to do anything
    } else {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            AlertDialog(
                onDismissRequest = {
                    onRequestPermission
                },
                title = { Text(text = "Camera Permission") },
                text = { Text(text = "Camera is required for the App Function") },
                confirmButton = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Button(onClick = onRequestPermission) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera"
                            )
                            Text(text = "Grant Permission")
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun checkUserFirstLogin(homeViewModel: HomeViewModel, context:Context, isUserStatusTrue: Boolean, onClose: () -> Unit) {
    var textValue = remember { mutableStateOf("") }
    // Define onClose to handle dialog dismissal
    var passwordValue = remember{ mutableStateOf("")}
    if (!isUserStatusTrue) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .background(Color.White)
            .clip(shape = RoundedCornerShape(15.dp))
        ) {
                Dialog(
                    onDismissRequest = onClose,
                ) {
                    Column(modifier = Modifier
                        ) {
                        Text(text="Initial Account Setup, Reset Default Email",
                            modifier = Modifier,
                            color = Color.White)
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(Shapes.small),
                            label = {
                                Text(
                                    text = "Enter Valid Email",
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
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true,
                            maxLines = 1,
                            value = textValue.value,
                            onValueChange = {
                                textValue.value = it
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Icon",
                                    tint = Color.White
                                )
                            },
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordTextFieldComponent(
                            labelValue = "Re-enter Password for Verification",
                            imageVector = Icons.Default.Lock,
                            onTextSelected = {
                                passwordValue.value = it
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ButtonComponent(
                            value="Reset Email",
                            onButtonClicked = {
                                /*
                                var emailStatus = resetEmail(textValue.value, passwordValue.value)
                                if (emailStatus != null && emailStatus is String) {
                                    // Show a success message
                                    homeViewModel.updateUserStatus()
                                    Toast.makeText(context, emailStatus, Toast.LENGTH_SHORT).show()

                                } else if (emailStatus != null) {
                                    // Show an error message
                                    Toast.makeText(context, "Error changing email. Error code: $emailStatus", Toast.LENGTH_SHORT).show()
                                }

                                 */
                                /*
                                checkEmailAvailability(textValue.value,
                                    onSuccess = {
                                        val emailStatus = resetEmail(textValue.value, passwordValue.value)
                                        if (emailStatus != null && emailStatus is String) {
                                            // Show a success message
                                            homeViewModel.updateUserStatus()
                                            Toast.makeText(context, emailStatus, Toast.LENGTH_SHORT).show()

                                        } else if (emailStatus != null) {
                                            // Show an error message
                                            Toast.makeText(context, "Error changing email. Error code: $emailStatus", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onError = { errorMessage ->
                                        println(errorMessage)
                                    })

                                 */

                            },
                            isEnabled=true)
                        Spacer(modifier = Modifier.height(10.dp))
                        ButtonComponent(
                            value = "Reset Later",
                            onButtonClicked = {
                                // Close the dialog without performing the reset
                                homeViewModel.updateUserStatus()
                            },
                            isEnabled = true
                        )
                        }

                    }
                }
            }
        }


@Composable
fun checkUserChangedPW(homeViewModel: HomeViewModel, context:Context, isUserStatusTrue: Boolean, hasUserChangedPW: Boolean, onClose: () -> Unit) {
    var textValue = remember { mutableStateOf("") }
    // Define onClose to handle dialog dismissal

    if (isUserStatusTrue && !hasUserChangedPW) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .background(Color.White)
            .clip(shape = RoundedCornerShape(15.dp))
        ) {
            Dialog(
                onDismissRequest = onClose,
            ) {
                Column(modifier = Modifier
                ) {
                    Text(text="Password Reset",
                        modifier = Modifier,
                        color = Color.White)
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.small),
                        label = {
                            Text(
                                text = "Email for Password Reset Link",
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        singleLine = true,
                        maxLines = 1,
                        value = textValue.value,
                        onValueChange = {
                            textValue.value = it
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Icon",
                                tint = Color.White
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonComponent(
                        value="Reset Password",
                        onButtonClicked = {
                            homeViewModel.resetPassword(
                                textValue.value,
                                onError = {errorMessage ->
                                          Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                },
                                onResetCompleted = {
                                    Toast.makeText(context, "Password reset email sent successfully.", Toast.LENGTH_SHORT).show()
                                }
                            ) },
                        isEnabled=true)
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonComponent(
                        value = "Reset Later",
                        onButtonClicked = {
                            // Close the dialog without performing the reset
                            homeViewModel.updateUserPWStatus()
                        },
                        isEnabled = true
                    )

                }

            }
        }
    }
}
/*
fun checkEmailAvailability(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
        .addOnSuccessListener { result ->
            if (result.signInMethods == null || result.signInMethods!!.isEmpty()) {
                // Email is not in use, proceed with the update
                onSuccess.invoke()
            } else {
                // Email is already in use
                onError.invoke("Email is already in use")
            }
        }
        .addOnFailureListener { e ->
            // Handle failure
            onError.invoke("Error checking email availability: ${e.message}")
        }
}

 */
fun resetEmail(email: String, passwordValue: String): String {
    val user = FirebaseAuth.getInstance().currentUser
    val userDocRef = FirebaseFirestore.getInstance().collection("users").document(user!!.uid)
    val currentEmail = FirebaseAuth.getInstance().currentUser?.email
    //val default = passwordValue
    val credential = EmailAuthProvider.getCredential(user?.email!!, passwordValue)


    return try{
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.verifyBeforeUpdateEmail(email)
                    Log.d(TAG, email)
                    // Proceed with email update
                } else {
                    // Handle reauthentication failure
                }
            }

        println("Email Verification Link Sent")
        //If successful, updated the changed_email field in users collection
        userDocRef.update("changed_email", true)
        println("Changed_email boolean changed")
        "Email Verification Link Sent"
    }catch (e: FirebaseAuthException) {
        // Return the error code in case of failure
        e.errorCode
    }
    catch (e: Exception) {
        println("Error changing email: $e")
        "Error changing email. Please check your email."
    }
}
@Preview
@Composable
fun DefaultPreviewTransitionScreen() {
    TransitionScreen()
}