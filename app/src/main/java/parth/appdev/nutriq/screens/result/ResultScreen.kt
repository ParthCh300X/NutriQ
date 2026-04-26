package parth.appdev.nutriq.presentation.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.model.RiskLevel
import parth.appdev.nutriq.ui.theme.*

@Composable
fun ResultScreen(food: Food) {

    val bg = MaterialTheme.colorScheme.background

    val (color, label) = when (food.riskLevel) {
        RiskLevel.SAFE -> SafeGreen to "SAFE"
        RiskLevel.MODERATE -> ModerateYellow to "MODERATE"
        RiskLevel.RISKY -> RiskRed to "RISKY"
        RiskLevel.UNKNOWN -> Color.Gray to "NO DATA"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🧾 Product Name (hero)
        Text(
            text = food.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🎯 Risk Badge (centerpiece)
        Card(
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = label,
                    color = color,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                if (food.riskLevel != RiskLevel.UNKNOWN) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (food.riskLevel) {
                            RiskLevel.SAFE -> "Clean composition"
                            RiskLevel.MODERATE -> "Watch consumption"
                            RiskLevel.RISKY -> "Frequent intake not recommended"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ⚠️ Reasons
        if (food.riskLevel == RiskLevel.UNKNOWN) {
            Text(
                text = "⚠️ Not enough data to evaluate this product",
                textAlign = TextAlign.Center
            )
        } else if (food.reasons.isNotEmpty()) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Why?",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                food.reasons.forEach {
                    Text("• $it")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 🧪 Ingredients Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = highlightIngredients(food.ingredients),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
fun highlightIngredients(text: String): AnnotatedString {

    val harmfulKeywords = listOf(
        "sugar", "syrup", "maltodextrin",
        "palm oil", "vegetable fat", "hydrogenated",
        "emulsifier", "lecithin", "flavour", "color"
    )

    val safeColor = Color.Unspecified
    val badColor = RiskRed

    return buildAnnotatedString {

        val words = text.split(" ")

        words.forEach { word ->

            val clean = word.lowercase().replace("[^a-z]".toRegex(), "")

            if (harmfulKeywords.any { clean.contains(it) }) {
                withStyle(
                    SpanStyle(
                        color = badColor,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("$word ")
                }
            } else {
                withStyle(SpanStyle(color = safeColor)) {
                    append("$word ")
                }
            }
        }
    }
}