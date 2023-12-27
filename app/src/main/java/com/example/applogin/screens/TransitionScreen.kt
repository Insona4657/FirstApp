package com.example.applogin.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.NavigationIcon
import com.example.applogin.data.NotificationModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter.getScreenForTitle
import com.example.applogin.loginflow.navigation.AppRouter.navigateTo
import com.example.applogin.loginflow.navigation.Screen
import com.example.applogin.loginflow.navigation.SystemBackButtonHandler
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
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
    var isNotificationDialogVisible by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(MyFirebaseMessagingService.getSavedNotifications(context)) }

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
                        isNotificationDialogVisible = true
                    }

                )
                if (isNotificationDialogVisible) {
                    NotificationDialog(
                        notifications = notifications.reversed(),
                        onDismiss = { isNotificationDialogVisible = false }
                    )
                }
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
                    onRequestPermission = notificationPermissionState::launchPermissionRequest)
                // Effect to trigger the checkAdminUser function on first composition
                val icons = listOf(
                    NavigationIcon("Warranty", R.drawable.warranty_logo),
                    NavigationIcon("Products", R.drawable.product_logo),
                    NavigationIcon("Inbox", R.drawable.inbox_logo),
                    NavigationIcon("Service Form", R.drawable.service_logo),
                )
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
                            .background(Color.White, shape= RoundedCornerShape(25.dp)),
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
fun NotificationDialog(notifications: List<NotificationModel>, onDismiss: () -> Unit) {
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
                items(notifications) { notification ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                buildAnnotatedString {
                                    val maxLengthTitle = 20 // Adjust the character limit as needed
                                    append(notification.title.take(maxLengthTitle))
                                    if (notification.title.length > maxLengthTitle) {
                                        withStyle(style = SpanStyle(color = Color.Black)) {
                                            append("... ")
                                        }
                                    }
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            // Limit the characters shown in notification.content
                            Text(
                                buildAnnotatedString {
                                    val maxLengthContent =
                                        30 // Adjust the character limit as needed
                                    append(notification.content.take(maxLengthContent))
                                    if (notification.content.length > maxLengthContent) {
                                        withStyle(style = SpanStyle(color = Color.Black)) {
                                            append("... (more)")
                                        }
                                    }
                                },
                                fontSize = 16.sp
                            )
                            Text(notification.timestamp)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreviewTransitionScreen() {
    TransitionScreen()
}