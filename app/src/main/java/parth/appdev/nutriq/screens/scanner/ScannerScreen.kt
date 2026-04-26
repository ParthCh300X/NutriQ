package parth.appdev.nutriq.presentation.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import parth.appdev.nutriq.presentation.screens.result.ResultScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scannedFood = viewModel.scannedFood.collectAsState()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasPermission) return

    // 🔥 Flash effect
    var showFlash by remember { mutableStateOf(false) }

    LaunchedEffect(scannedFood.value) {
        if (scannedFood.value != null) {
            showFlash = true
            delay(300)
            showFlash = false
        }
    }

    // 🎬 Smooth transition between scanner ↔ result
    AnimatedContent(
        targetState = scannedFood.value,
        transitionSpec = {
            fadeIn(tween(300)) + scaleIn(initialScale = 0.95f) togetherWith
                    fadeOut(tween(200))
        },
        label = "scanner_transition"
    ) { food ->

        if (food != null) {

            // 🎯 RESULT MODE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                Button(
                    onClick = { viewModel.reset() }
                ) {
                    Text("← Scan Again")
                }

                Spacer(modifier = Modifier.height(12.dp))

                ResultScreen(food = food)
            }

        } else {

            // 📸 SCANNER MODE
            Box(modifier = Modifier.fillMaxSize()) {

                CameraPreview(viewModel)

                ScannerOverlay()

                // 🔥 Green flash feedback
                AnimatedVisibility(
                    visible = showFlash,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Green.copy(alpha = 0.25f))
                    )
                }

                // 🧭 Instruction text
                Text(
                    text = "Align barcode within frame",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 120.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}