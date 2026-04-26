package parth.appdev.nutriq.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.*
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.model.RiskLevel
import parth.appdev.nutriq.presentation.screens.home.HomeScreen
import parth.appdev.nutriq.presentation.screens.scanner.ScannerScreen
import parth.appdev.nutriq.presentation.screens.history.HistoryScreen
import parth.appdev.nutriq.presentation.screens.result.ResultScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    // 🔥 LOCAL STATE (this replaces Parcelable + nav passing)
    var selectedFood by remember { mutableStateOf<Food?>(null) }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(NavRoutes.Home.route) {
                HomeScreen(
                    onScanClick = {
                        navController.navigate(NavRoutes.Scanner.route)
                    }
                )
            }

            composable(NavRoutes.Scanner.route) {
                ScannerScreen()
            }

            composable(NavRoutes.History.route) {

                if (selectedFood != null) {

                    Column {
                        Button(onClick = { selectedFood = null }) {
                            Text("← Back")
                        }

                        ResultScreen(food = selectedFood!!)
                    }

                } else {

                    HistoryScreen(
                        onItemClick = { item ->

                            selectedFood = Food(
                                name = item.name,
                                ingredients = item.ingredients,
                                riskLevel = try {
                                    RiskLevel.valueOf(item.risk)
                                } catch (e: Exception) {
                                    RiskLevel.UNKNOWN
                                },
                                reasons = emptyList()
                            )
                        }
                    )
                }
            }
        }
    }
}