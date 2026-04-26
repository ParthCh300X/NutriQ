package parth.appdev.nutriq.presentation.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import parth.appdev.nutriq.presentation.screens.result.ResultScreen

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsState()

    val context = LocalContext.current

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

    // ✅ SINGLE DECISION POINT (THIS FIXES EVERYTHING)

    when {
        !hasPermission -> {
            // Do nothing (permission dialog already triggered)
        }

        state.value != null -> {
            ResultScreen(food = state.value!!)
        }

        else -> {
            CameraPreview(viewModel)
        }
    }
}