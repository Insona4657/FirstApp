package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.data.ProfileViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePageScreen(homeViewModel: HomeViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {
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
                mainAppBar(toolbarTitle = "Profile Page",
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
                Column(
                    modifier = Modifier,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        mainbackground()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Render user details
                            if (user != null) {
                                NormalTextComponent(introText = "Name: ${user?.displayName}")
                                NormalTextComponent(introText = "Email: ${user?.email}")
                                // You can display other user details as needed
                            } else {
                                // Handle case where user details are not available
                                Text(text = "User details not available")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewProfilePageScreen() {
    ProfilePageScreen()
}