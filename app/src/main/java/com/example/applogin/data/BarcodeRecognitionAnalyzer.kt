package com.example.applogin.data

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.unit.dp
import com.google.android.play.core.integrity.e
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.math.min

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
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            // extract image from image proxy and close the image proxy to free resources to analyze next image
            val mediaImage: Image = imageProxy.image ?: run {imageProxy.close(); return@launch }//Original input image
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
/*

                // Calculate crop coordinates
                val targetWidthDp = 300
                val targetHeightDp = 250

                val targetWidth = (targetWidthDp * Resources.getSystem().displayMetrics.density).toInt()
                val targetHeight = (targetHeightDp * Resources.getSystem().displayMetrics.density).toInt()

                val x = (mediaImage.width - targetWidth) / 2
                val y = (mediaImage.height - targetHeight) / 2

                // Create a cropped Bitmap
                val croppedBitmap = Bitmap.createBitmap(mediaImage.width, mediaImage.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(croppedBitmap)
                val srcRect = Rect(x, y, x + targetWidth, y + targetHeight)
                val destRect = Rect(0, 0, targetWidth, targetHeight)
                canvas.drawBitmap(mediaImage.toBitmap(), srcRect, destRect, null)

                // Rotate the cropped Bitmap based on the image rotation
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                val rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.width, croppedBitmap.height, matrix, true)

                // Create a new InputImage from the cropped and rotated Bitmap
                val inputImage = InputImage.fromBitmap(rotatedBitmap, rotationDegrees)

                val barcodes = barcodeRecognizer.process(inputImage).await()
                val barcodeValues = barcodes.mapNotNull { it.displayValue }

                if (barcodeValues.isNotEmpty()) {
                    onBarcodeDetected(barcodeValues)
                }
            } catch (e: Exception) {
                // Handle exceptions appropriately
                e.printStackTrace()
            } finally {
                imageProxy.close()
            }
        }
    }
}

 */





@OptIn(ExperimentalGetImage::class)
fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}