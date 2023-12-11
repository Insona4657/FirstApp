package com.example.applogin.data

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyser(
    val callback: (List<String>) -> Unit
): ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val barcodeValues = barcodes.mapNotNull { it.displayValue }
                    if (barcodeValues.isNotEmpty()) {
                        callback(barcodeValues)
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    Log.d("FAILURE", "SCANNER FAILED")
                    // ...
                }
        }
        imageProxy.close()
    }
}
