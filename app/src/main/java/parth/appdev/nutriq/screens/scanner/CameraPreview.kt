package parth.appdev.nutriq.presentation.screens.scanner

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(viewModel: ScannerViewModel) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 🔒 Prevent multiple scans
    var hasScanned by remember { mutableStateOf(false) }

    // 🔥 Stable ML scanner
    val barcodeScanner = remember { BarcodeScanning.getClient() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context)
                ) { imageProxy ->

                    val mediaImage = imageProxy.image

                    if (mediaImage != null && !hasScanned) {

                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->

                                for (barcode in barcodes) {
                                    val rawValue = barcode.rawValue

                                    if (!hasScanned && rawValue != null) {
                                        hasScanned = true

                                        Log.d("NutriQ", "Barcode: $rawValue")

                                        // 🔔 Visible feedback (VERY IMPORTANT)
                                        Toast.makeText(
                                            context,
                                            "Scanned: $rawValue",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // 🚀 Trigger API call
                                        viewModel.fetchProduct(rawValue)
                                        break
                                    }
                                }

                            }
                            .addOnFailureListener {
                                Log.e("NutriQ", "Scan failed", it)
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }

                    } else {
                        imageProxy.close()
                    }
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("NutriQ", "Camera binding failed", e)
                }

            }, ContextCompat.getMainExecutor(context))
        }
    )
}