package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.BarcodeRecognitionAnalyzer
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun BarcodeScannerScreen(homeViewModel: HomeViewModel = viewModel(), ) {
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
                mainAppBar(toolbarTitle = "Barcode & QR Scanner",
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
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CameraContent()
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
) {
    if (hasPermission) {
        CameraContent()
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
private fun CameraContent() {
    var isScanning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    // State to hold the list of barcodes
    var barcodeValues by remember { mutableStateOf(emptyList<String>()) }


    Scaffold(
        modifier = Modifier.fillMaxSize())
    { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Use Box to overlay the line on top of the PreviewView
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = androidx.compose.ui.Alignment.BottomCenter
            ) {
                if (isScanning) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
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
                                    }
                                )
                            }
                        }
                    )
                }
                Box {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(32.dp)
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
                        items(barcodeValues) { barcode ->
                            Text(
                                text = "Barcode: $barcode",
                                color = Color.Black
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            .padding(16.dp)
                    ) {
                        Text(text = if (isScanning) "Stop Scanning" else "Start Scanning")
                    }
                    // Button to copy barcodes to clipboard
                    Button(
                        onClick = {
                            copyBarcodesToClipboard(context, barcodeValues)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = "Copy Barcodes")
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
