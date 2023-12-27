package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.BarcodeRecognitionAnalyzer
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeScannerScreen(homeViewModel: HomeViewModel = viewModel(), ) {
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // Initialize MediaPlayer outside the composable function
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.beep1) }
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
                mainAppBar(toolbarTitle = "Scanner",
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
                //To enable the background function to work
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CameraPermission(hasPermission = cameraPermissionState.status.isGranted,
                        onRequestPermission = cameraPermissionState::launchPermissionRequest, mediaPlayer = mediaPlayer)
                }
                }
            }
        }
    }

@Composable
fun NoPermissionContent(onRequestPermission: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Camera permission is required to access the scanner")
        Button(onClick = onRequestPermission) {
            Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
            Text(text = "Grant Permission")
        }
    }
}

@Composable
fun CameraPermission(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    mediaPlayer: MediaPlayer,
) {
    if (hasPermission) {
        CameraContent(mediaPlayer = mediaPlayer)
    } else {
        //Maybe change to main screen if the don't accept permissions
        NoPermissionScreen(onRequestPermission)
    }
}

@Composable
fun NoPermissionScreen(onRequestPermission: () -> Unit) {
    NoPermissionContent(
        onRequestPermission = onRequestPermission
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun CameraContent(mediaPlayer: MediaPlayer) {
    var isScanning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    // State to hold the list of barcodes
    var barcodeValues by remember { mutableStateOf(emptyList<String>()) }
    var barcodeSelected by remember {mutableStateOf("")}

    Scaffold(
        modifier = Modifier.fillMaxWidth())
    { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Use Box to overlay the line on top of the PreviewView
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isScanning) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        AndroidView(
                            modifier = Modifier
                                .padding(paddingValues),
                            factory = { context ->
                                PreviewView(context).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    setBackgroundColor(android.graphics.Color.BLACK)
                                    scaleType = PreviewView.ScaleType.FILL_START
                                }.also { previewView ->
                                    // Setup the scanner to continuously scan barcodes
                                    startBarcodeScanner(
                                        context = context,
                                        cameraController = cameraController,
                                        lifecycleOwner = lifecycleOwner,
                                        previewView = previewView,
                                        onBarcodeDetected = { barcode ->
                                            println("Detected barcode: $barcode")
                                            barcodeValues = barcodeValues + barcode
                                            isScanning = !isScanning
                                            // Play the sound after scanning is done
                                            mediaPlayer.start()
                                            // check the barcode with the database and preview in a popup dialog the warranty
                                            // of the device
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
            Column(modifier = Modifier.background(color = Color.White)) {
                // State to track the clicked index
                var clickedIndex by remember { mutableStateOf(-1) }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                        .height(150.dp) // Set the initial height
                ) {
                    // Display the barcode information
                    item {
                        Text(
                            text = "Barcodes Scanned: ${barcodeValues.size}",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Display individual barcodes
                    itemsIndexed(barcodeValues) {index, barcode ->
                        Text(
                            text = "$barcode",
                            color = if (index == clickedIndex) Color.Blue else Color.Black,
                            modifier = Modifier.clickable {
                                // Toggle the clicked state when clicked
                                clickedIndex = if (index == clickedIndex) -1 else index
                                barcodeSelected = barcode
                                // Start Search based on IMEI and display the popup
                                //startSearchBasedOnIMEI(barcode)
                                mediaPlayer.start()
                            }
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //Start Scanner and Stop Scanner Button
                        Button(
                            onClick = {
                                isScanning = !isScanning
                                if (!isScanning) {
                                    stopScanner(cameraController)
                                }
                            },
                            modifier = Modifier
                                .weight(1f) // Adjust the weight as needed
                                .padding(2.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = if (isScanning) "Stop" else "Start")
                        }
                        // Button to copy barcodes to clipboard
                        Button(
                            onClick = {
                                copyBarcodesToClipboard(context, barcodeValues)
                            },
                            modifier = Modifier
                                .weight(1f) // Adjust the weight as needed
                                .padding(2.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = "Copy")
                        }
                        // Button to Clear the list of Barcodes Scanned
                        Button(
                            onClick = {
                                barcodeValues = emptyList()
                            },
                            modifier = Modifier
                                .weight(1f) // Adjust the weight as needed
                                .padding(2.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = "Clear")
                        }
                        // Button to Search the Details of Imei in the list
                        Button(
                            onClick = {
                                //TODO Function to search IMEI later
                            },
                            modifier = Modifier
                                .weight(1f) // Adjust the weight as needed
                                .padding(2.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = "Search")
                        }
                    }
                }
            }
        }
    }
}
fun copyBarcodesToClipboard(context: Context, barcodes: List<String>) {
    val clipboardManager = ContextCompat.getSystemService(
        context,
        ClipboardManager::class.java
    )

    val barcodeText = barcodes.joinToString("\n")
    val clip = ClipData.newPlainText("Barcodes", barcodeText)

    clipboardManager?.setPrimaryClip(clip)

    Toast.makeText(
        context,
        "Barcodes copied to clipboard",
        Toast.LENGTH_SHORT
    ).show()
}

private fun stopScanner(cameraController: LifecycleCameraController) {
    // Additional cleanup or actions when stopping the scanner
    cameraController.unbind()
}
fun startBarcodeScanner(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onBarcodeDetected: (List<String>) -> Unit,
) {
    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        BarcodeRecognitionAnalyzer{ barcodes ->
                onBarcodeDetected(barcodes)
            }
    )
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}
