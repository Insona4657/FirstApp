package com.example.applogin.data

import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BarcodeRecognitionAnalyzer(
    private var onBarcodeDetected: (List<String>) -> Unit
) : ImageAnalysis.Analyzer {
    companion object {
        const val THROTTLE_TIMEOUT_MS = 1_000L
    }
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val barcodeRecognizer = BarcodeScanning.getClient(options)
    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            // extract image from image proxy and close the image proxy to free resources to analyze next image
            val mediaImage: Image = imageProxy.image ?: run {imageProxy.close(); return@launch }
            val inputImage: InputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            suspendCoroutine { continuation ->
                barcodeRecognizer.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        val barcodeValues = barcodes.mapNotNull { it.displayValue }
                        if (barcodeValues.isNotEmpty()) {
                            onBarcodeDetected(barcodeValues)
                        }
                    }
                    .addOnCompleteListener {
                        // Check if barcode processing is complete before resuming the continuation
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        // Handle failure if needed
                        continuation.resumeWithException(e)
                        println("No Barcode Detected")
                    }
            }
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}
