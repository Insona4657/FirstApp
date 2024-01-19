package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.data.ProfileViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@Composable
fun MyProductScreen(homeViewModel: HomeViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    NavigationDrawerHeader(homeViewModel)
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
                mainAppBar(toolbarTitle = "My Product",
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
                        Column(modifier = Modifier) {

                        }
                    }
                }
            }
        }
    }
}
