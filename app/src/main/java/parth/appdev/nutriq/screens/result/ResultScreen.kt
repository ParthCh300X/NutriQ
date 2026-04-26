package parth.appdev.nutriq.presentation.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.model.RiskLevel
import parth.appdev.nutriq.ui.theme.*

@Composable
fun ResultScreen(food: Food) {

    val color = when (food.riskLevel) {
        RiskLevel.SAFE -> SafeGreen
        RiskLevel.MODERATE -> ModerateYellow
        RiskLevel.RISKY -> RiskRed
        RiskLevel.UNKNOWN -> Gray
    }

    val label = when (food.riskLevel) {
        RiskLevel.UNKNOWN -> "NO DATA"
        else -> food.riskLevel.name
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = food.name,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (food.riskLevel == RiskLevel.UNKNOWN) {
            Text(
                text = "⚠️ Unable to analyze this product",
                textAlign = TextAlign.Center
            )
        } else {
            food.reasons.forEach {
                Text("• $it")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = food.ingredients,
            textAlign = TextAlign.Center
        )
    }
}