package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch

@Composable
fun BarcodeScannerScreen(homeViewModel: HomeViewModel = viewModel(), scannerActivity: AppCompatActivity = AppCompatActivity(), barcodeScannerViewModel: AppCompatActivity = AppCompatActivity()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isScannerButtonClicked by remember { mutableStateOf(false) }

    // Use rememberLauncherForActivityResult to obtain the launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, start scanner
                isScannerButtonClicked = true
                //barcodeScannerViewModel
            } else {
                // Permission not granted, handle accordingly
            }
        }
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
                mainAppBar(toolbarTitle = "Warranty",
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
                    }
                )
            },

            ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues),
                //To enable the background function to work
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier,
                ) {
                    if (isScannerButtonClicked) {
                        // Render the ScannerActivity when the button is clicked
                        //AndroidView(){


                        }
                    }
                        // Render the BarcodeScannerScreen
                        //Button(onClick = {
                        //    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        //}) {
                         //   Text(text = "OPEN SCANNER")
                        //}

                }
            }
        }
    }
