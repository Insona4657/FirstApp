@file:JvmName("InboxScreenKt")

package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.ServiceRequestForm
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.ProfileViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(homeViewModel: HomeViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {
    val user by profileViewModel.user.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    NavigationDrawerHeader()
                    NavigationDrawerBody(
                        navigationDrawerItems = homeViewModel.navigationItemsList,
                        onClick = {
                            Log.d(ContentValues.TAG, "Inside NavigationDrawer")
                            Log.d(ContentValues.TAG, "Inside ${it.itemId} ${it.title}")
                            AppRouter.navigateTo(AppRouter.getScreenForTitle(it.title))
                        })
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
                        //TODO
                    }
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
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 40.dp)
                            .background(Color.White, shape = RoundedCornerShape(25.dp))

                    ) {
                        mailboxComponent()
                    }

                }
            }
        }
    }
}
@Composable
fun mailboxComponent() {
    LazyColumn(){
        items(10){
            individualMessage()
        }
    }
}
@Composable
fun individualMessage() {
    var isRead by remember{ mutableStateOf(false) }
    var showDialog by remember{ mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .bottomShadow(5.dp)
            .clickable { showDialog = true }
            /*.pointerInput(Unit) {
                detectTransformGestures { _, pan, _ ->
                    if (pan != Offset(0f, 0f)) {
                        // Long press detected, implement your logic here
                        // For example, show delete confirmation box
                    }
                }
            }*/
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (isRead) Color.Gray else Color(0xFFFF9820),
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
                        Text("Message Title", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Message Body", fontSize = 16.sp)
                    }
                    Box {
                    // Orange round icon (visibility based on read/unread state)
                    if (!isRead) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailUnread,
                            contentDescription = null,
                            tint = Color(0xFFFF9820), // Orange color
                            modifier = Modifier,
                        )
                    }
                }
            }
        }

        // Dialog to show message details
        if (showDialog) {
            MessageDialog(
                title = "Message Title",
                body = "Message Body",
                onClose = { showDialog = false }
            )
        }
    }
}

@Composable
fun MessageDialog(title: String, body: String, onClose: () -> Unit) {
    // Implement the dialog content here
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