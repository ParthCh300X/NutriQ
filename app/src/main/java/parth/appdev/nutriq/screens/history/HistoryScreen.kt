package parth.appdev.nutriq.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import parth.appdev.nutriq.data.local.entity.FoodEntity
import parth.appdev.nutriq.presentation.common.UiState
import parth.appdev.nutriq.ui.theme.ModerateYellow
import parth.appdev.nutriq.ui.theme.RiskRed
import parth.appdev.nutriq.ui.theme.SafeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onItemClick: (barcode: String) -> Unit
) {
    val historyState by viewModel.historyState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Scan History") }) }) { padding ->

        when (val state = historyState) {

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.NotFound -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No scans yet", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Start scanning to build your health insights", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.data) { entity ->
                        HistoryCard(entity = entity, onClick = { onItemClick(entity.barcode) })
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun HistoryCard(entity: FoodEntity, onClick: () -> Unit) {
    val (color, label) = when (entity.risk) {
        "SAFE" -> SafeGreen to "SAFE"
        "MODERATE" -> ModerateYellow to "MODERATE"
        "RISKY" -> RiskRed to "RISKY"
        else -> Color.Gray to "UNKNOWN"
    }

    val timeText = remember(entity.timestamp) {
        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(entity.timestamp))
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(color, shape = MaterialTheme.shapes.small))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entity.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text(label, color = color, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(2.dp))
                Text(timeText, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
            Text("›", style = MaterialTheme.typography.titleLarge)
        }
    }
}