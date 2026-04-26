package parth.appdev.nutriq.domain.model

data class Food(
    val name: String,
    val ingredients: String,
    val riskLevel: RiskLevel,
    val reasons: List<String>
)