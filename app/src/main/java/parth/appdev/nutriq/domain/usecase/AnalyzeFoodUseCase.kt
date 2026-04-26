package parth.appdev.nutriq.domain.usecase

import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.model.RiskLevel

class AnalyzeFoodUseCase {

    fun execute(name: String?, ingredients: String?): Food {

        if (name.isNullOrBlank() || ingredients.isNullOrBlank()) {
            return Food(
                name = name ?: "Unknown Product",
                ingredients = "No data available",
                riskLevel = RiskLevel.UNKNOWN,
                reasons = listOf("Insufficient data to analyze")
            )
        }

        val ing = ingredients.lowercase()
        var score = 0
        val reasons = mutableListOf<String>()

        if (ing.contains("sugar") || ing.contains("syrup") || ing.contains("maltodextrin")) {
            score += 2
            reasons.add("Contains added sugars")
        }

        if (ing.contains("palm oil") || ing.contains("vegetable fat") || ing.contains("hydrogenated")) {
            score += 2
            reasons.add("Contains processed fats")
        }

        if (ing.contains("emulsifier") || ing.contains("lecithin") || ing.contains("flavour")) {
            score += 1
            reasons.add("Contains additives")
        }

        val level = when {
            score >= 4 -> RiskLevel.RISKY
            score >= 2 -> RiskLevel.MODERATE
            else -> RiskLevel.SAFE
        }

        return Food(
            name = name,
            ingredients = ingredients,
            riskLevel = level,
            reasons = reasons
        )
    }
}