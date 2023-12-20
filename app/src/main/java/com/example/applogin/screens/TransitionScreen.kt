package com.example.applogin.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.AppRouter.getScreenForTitle
import com.example.applogin.loginflow.navigation.AppRouter.navigateTo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TransitionScreen(homeViewModel: HomeViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showNotificationDialog = remember { mutableStateOf(false)}

    val notificationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    var isNotificationDialogVisible by remember { mutableStateOf(false) }
    var notificationMessages by remember { mutableStateOf(listOf("Notification 1", "Notification 2")) }


    if (showNotificationDialog.value) FirebaseMessagingNotificationPermissionDialog(
        showNotificationDialog = showNotificationDialog,
        notificationPermissionState = notificationPermissionState
    )

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet{
                Column{
                    NavigationDrawerHeader()
                    NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList, onClick = {
                        Log.d(TAG, "Inside NavigationDrawer")
                        Log.d(TAG, "Inside ${it.itemId} ${it.title}")
                        navigateTo(getScreenForTitle(it.title))
                    })
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
                        isNotificationDialogVisible = true
                    }

                )
                if (isNotificationDialogVisible) {
                NotificationDialog(
                    notifications = notificationMessages,
                    onDismiss = { isNotificationDialogVisible = false }
                )
            }
            },

            ){ paddingValues ->
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                color = MaterialTheme.colorScheme.background,
                ) {

                MainPageTopBackground(
                    topimage =R.drawable.top_background,
                    middleimage = R.drawable.middle_background,
                    bottomimage = R.drawable.bottom_background)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // First Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 20.dp
                            )
                            .clip(RoundedCornerShape(25.dp)),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f) // Equal weight for both boxes
                                .clip(RoundedCornerShape(25.dp))
                        ) {
                            navigationIcon(
                                stringResource(R.string.warranty_search),
                                pageIcon = painterResource(
                                    id = R.drawable.warranty_logo),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside warranty_search")
                                    navigateTo(getScreenForTitle("Warranty Search"))
                                })
                        }
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f) // Equal weight for both boxes
                                .clip(RoundedCornerShape(25.dp)),
                            //.background(Color.White),
                        ) {
                            navigationIcon(
                                stringResource(
                                    R.string.products),
                                pageIcon = painterResource(id = R.drawable.product_logo),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside products")
                                    navigateTo(getScreenForTitle("Products Page"))
                                }
                            )
                        }
                    }

                    // Second Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 20.dp,
                            )
                            .clip(RoundedCornerShape(25.dp)),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f) // Equal weight for both boxes
                                .clip(RoundedCornerShape(25.dp)),
                        ) {
                            navigationIcon(
                                pageTitle = "Inbox",
                                pageIcon = painterResource(
                                    id = R.drawable.inbox_logo),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside Inbox")
                                    navigateTo(getScreenForTitle("Inbox"))
                                })
                        }
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f) // Equal weight for both boxes
                                .clip(RoundedCornerShape(25.dp)),
                        ) {
                            navigationIcon(
                                pageTitle = ("Service Request"),
                                pageIcon = painterResource(
                                    id = R.drawable.service_logo),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside services")
                                    navigateTo(getScreenForTitle("Service Request"))
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationDialog(notifications: List<String>, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 60.dp,
                    bottom = 40.dp,
                )
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .background(Color.White, shape = RoundedCornerShape(15.dp))
        ) {
            LazyColumn {
                items(notifications) { message ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(text = message, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FirebaseMessagingNotificationPermissionDialog(
    showNotificationDialog: MutableState<Boolean>,
    notificationPermissionState: PermissionState
) {
    if(showNotificationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog.value = false
                notificationPermissionState.launchPermissionRequest()
            },
            title = { Text(text = "Notification Permission")},
            text = { Text(text = "Notification is required for the App Function")},
            confirmButton = {
                TextButton(onClick = {
                    showNotificationDialog.value = false
                    notificationPermissionState.launchPermissionRequest()
                    //Firebase.messaging.subscribetoTopic("")
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

@Preview
@Composable
fun DefaultPreviewTransitionScreen() {
    TransitionScreen()
}