package com.example.applogin.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.hardware.camera2.params.BlackLevelPattern
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.BarcodeAnalyser
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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

//Sample Code for implementing the qr scanner
/*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeScanner(navController: navController, barcodeScannerViewModel: BarcodeScannerViewModel) {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    CameraPermission(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
    val context = LocalContext.current as Activity
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val analysisUseCase: ImageAnalysis = ImageAnalysis.Builder().build()
    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_CODE_39,
            //Others Barcode types to be added if needed
        ).build()
    val scanner = BarcodeScanning.getClient(options)
    processImageProxy()
}
@Composable
fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    cameraProvider: ProcessCameraProvider
) {
    imageProxy.image?.let { image ->
        val inputImage =
            InputImage.fromMediaImage(
                image,
                imageProxy.imageInfo.rotationDegrees
            )

        barcodeScanner.process(inputImage)
            .addOnsuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)

                // "rawValue" is the decoded value of the barcode
                barcode?.rawValue?.let { value ->

                }
            }
    }

}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also{ cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
*/

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

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val barcodeScanner = remember { BarcodeScanning.getClient() }
    val cameraExecutor = Executors.newSingleThreadExecutor()

    // State to hold the list of barcodes
    var barcodeValues by remember { mutableStateOf(emptyList<String>()) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
            paddingValues: PaddingValues ->
        AndroidView(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
            factory = {context ->
                PreviewView(context).apply{
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    setBackgroundColor(android.graphics.Color.BLACK)
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also {
                        previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)

                    //Adding new code from here
                    val imageCapture = ImageCapture.Builder().build()
                    val imageAnalyzer = ImageAnalysis.Builder()
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, BarcodeAnalyser{barcodes ->
                                // Update the list of barcodes
                                barcodeValues = barcodes
                            })
                        }
                }
            })
            // Display the barcodes
            if (barcodeValues.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.ui.graphics.Color.Gray)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    for (barcode in barcodeValues) {
                        Text(text = "Barcode: $barcode", color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
    }
}