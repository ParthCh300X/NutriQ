package parth.appdev.nutriq.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import parth.appdev.nutriq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onItemClick: (HistoryItem) -> Unit
) {

    val history = viewModel.history.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Scan History")
                }
            )
        }
    ) { padding ->

        if (history.value.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "No scans yet",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Start scanning to build your health insights",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(history.value) { item ->

                val (color, label) = when (item.risk) {
                    "SAFE" -> SafeGreen to "SAFE"
                    "MODERATE" -> ModerateYellow to "MODERATE"
                    "RISKY" -> RiskRed to "RISKY"
                    else -> Color.Gray to "UNKNOWN"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) },
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color, shape = MaterialTheme.shapes.small)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = label,
                                color = color,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = "›",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}