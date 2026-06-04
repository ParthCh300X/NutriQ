package parth.appdev.nutriq.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import parth.appdev.nutriq.presentation.common.UiState
import parth.appdev.nutriq.presentation.screens.result.ResultScreen
import parth.appdev.nutriq.presentation.screens.scanner.CameraPreview
import parth.appdev.nutriq.presentation.screens.scanner.ScannerOverlay
import parth.appdev.nutriq.ui.theme.RiskRed

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scanState by viewModel.scanState.collectAsState()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    if (!hasPermission) return

    var showFlash by remember { mutableStateOf(false) }
    LaunchedEffect(scanState) {
        if (scanState is UiState.Success) {
            showFlash = true
            delay(300)
            showFlash = false
        }
    }

    AnimatedContent(
        targetState = scanState,
        transitionSpec = {
            fadeIn(tween(300)) + scaleIn(initialScale = 0.95f) togetherWith fadeOut(tween(200))
        },
        label = "scanner_transition"
    ) { state ->

        when (state) {

            is UiState.Idle -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CameraPreview(viewModel)
                    ScannerOverlay()

                    AnimatedVisibility(visible = showFlash, enter = fadeIn(), exit = fadeOut()) {
                        Box(modifier = Modifier.fillMaxSize().background(Color.Green.copy(alpha = 0.25f)))
                    }

                    Text(
                        text = "Align barcode within frame",
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("Analyzing product…", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            is UiState.Success -> {
                Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    Button(onClick = { viewModel.reset() }) { Text("← Scan Again") }
                    Spacer(Modifier.height(12.dp))
                    ResultScreen(food = state.data)
                }
            }

            is UiState.NotFound -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text("Product not found", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "This barcode isn't in the Open Food Facts database yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { viewModel.reset() }) { Text("Try Another") }
                    }
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text("Something went wrong", style = MaterialTheme.typography.titleLarge, color = RiskRed)
                        Spacer(Modifier.height(8.dp))
                        Text(state.message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { viewModel.reset() }) { Text("Try Again") }
                    }
                }
            }
        }
    }
}