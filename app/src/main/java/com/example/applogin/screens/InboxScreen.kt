@file:JvmName("InboxScreenKt")

package com.example.applogin.screens

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.ServiceRequestForm
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.NotificationModel
import com.example.applogin.data.ProfileViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch
import com.google.accompanist.insets.ProvideWindowInsets


@Composable
fun InboxScreen(
    homeViewModel: HomeViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val user by profileViewModel.user.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Observe changes in notifications from MyFirebaseMessagingService
    // Use state to hold notifications
    var notifications by remember { mutableStateOf(MyFirebaseMessagingService.getSavedNotifications(context)) }
    // Observe changes in notifications
    ProvideWindowInsets {
        ModalNavigationDrawer(
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet {
                    Column {
                        NavigationDrawerHeader()
                        NavigationDrawerBody(
                            navigationDrawerItems = homeViewModel.navigationItemsList
                        ) {
                            Log.d(ContentValues.TAG, "Inside NavigationDrawer")
                            Log.d(ContentValues.TAG, "Inside ${it.itemId} ${it.title}")
                            AppRouter.navigateTo(AppRouter.getScreenForTitle(it.title))
                        }
                    }
                }
            }, drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    mainAppBar(toolbarTitle = "Inbox",
                        logoutButtonClicked = {
                            homeViewModel.logout()
                        },
                        navigationIconClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        barcodeIconClicked = {
                            AppRouter.navigateTo(AppRouter.getScreenForTitle("Barcode Scanner"))
                        },
                        notificationIconClicked = {
                            AppRouter.navigateTo(AppRouter.getScreenForTitle("Inbox"))
                        },
                    )
                },

                ) { paddingValues ->
                Surface(
                    modifier = Modifier
                        .padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPageTopBackground(
                        topimage = R.drawable.product_category_banner,
                        middleimage = R.drawable.middle_background,
                        bottomimage = R.drawable.bottom_background
                    )
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp, bottom = 30.dp, start = 10.dp, end = 10.dp),
                        ) {
                            ServiceRequestForm(introText = "Inbox")
                            Button(modifier = Modifier.padding(20.dp),
                                onClick = {
                                    MyFirebaseMessagingService.clearAllSharedPreferences(context)
                                    // Update notifications when messages are deleted
                                    notifications = MyFirebaseMessagingService.getSavedNotifications(context)
                                }
                            ){
                                Text("Clear All Messages")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 40.dp)
                                .background(Color.White, shape = RoundedCornerShape(25.dp))

                        ) {
                            LazyColumn() {
                                Log.d(
                                    "InboxScreen",
                                    "Number of notifications: ${notifications.size}"
                                )
                                items(notifications.reversed()) { notification ->
                                    individualMessage(notification = notification,
                                        onDialogClose = {
                                            notifications = MyFirebaseMessagingService.getSavedNotifications(context)
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun individualMessage(notification: NotificationModel,
                      onDialogClose: () -> Unit) {
    var isRead = notification.read
    var showDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .clickable {
                // Set isRead to true when clicked
                MyFirebaseMessagingService.markNotificationAsRead(context, notification.timestamp)
                // Show the dialog
                showDialog = true
            }
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDeleting) Color.Red else Color.Transparent) // Add a background to indicate delete mode
            .bottomShadow(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (isRead) Color.LightGray else Color(0xFFFF9820),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
                .clip(RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Column {
                    // Limit the characters shown in notification.title
                    Text(
                        buildAnnotatedString {
                            val maxLengthTitle = 20 // Adjust the character limit as needed
                            append(notification.title.take(maxLengthTitle))
                            if (notification.title.length > maxLengthTitle) {
                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                    append("...")
                                }
                            }
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    // Limit the characters shown in notification.content
                    Text(
                        buildAnnotatedString {
                            val maxLength = 50 // Adjust the character limit as needed
                            append(notification.content.take(maxLength))
                            if (notification.content.length > maxLength) {
                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                    append("... (more)")
                                }
                            }
                        },
                        fontSize = 16.sp
                    )
                    Text(notification.timestamp)
                }
                Box {
                    // Orange round icon (visibility based on read/unread state)
                    if (!isRead) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailUnread,
                            contentDescription = null,
                            tint = if (isRead) Color.Gray else Color(0xFFFF9820),
                            modifier = Modifier,
                        )
                    }
                }
            }
        }
        // Show the dialog if showDialog is true
        if (showDialog) {
            MessageDialog(
                title = notification.title,
                body = notification.content,
                onClose = {
                    // Close the dialog
                    showDialog = false

                    // Trigger recomposition by invoking the onDialogClose callback
                    onDialogClose()
                }
            )
        }
    }
}

@Composable
fun MessageDialog(title: String, body: String, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(bottom = 10.dp)
        ) {
            Row{
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 20.dp, bottom = 20.dp, start = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.notification_bell),
                        contentDescription = "Cross Icon"
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .padding(20.dp),
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = body,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
            Button(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp),
            ) {
                Text(text = "OK")
            }
        }
    }
    /*
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onClose,
        title = { Text(text = title) },
        text = { Text(text = body,
            modifier = Modifier.padding(start = 2.dp)) },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFFFF9820) // Orange color
                )
            ) {
                Text("OK")
            }
        }
    )
     */
}

// Custom Modifier for bottom shadow
fun Modifier.bottomShadow(elevation: Dp): Modifier = drawWithContent {
    drawContent()

    // Draw the shadow at the bottom
    drawRect(
        color = Color.Black.copy(alpha = 0.2f),
        size = Size(size.width, elevation.toPx()),
        topLeft = Offset(0f, size.height - elevation.toPx())
    )
}
@Preview
@Composable
fun DefaultPreviewInboxScreen() {
    InboxScreen()
}