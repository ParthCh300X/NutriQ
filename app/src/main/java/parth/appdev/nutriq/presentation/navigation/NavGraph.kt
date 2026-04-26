package parth.appdev.nutriq.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import parth.appdev.nutriq.presentation.screens.home.HomeScreen
import parth.appdev.nutriq.presentation.screens.scanner.ScannerScreen
import parth.appdev.nutriq.presentation.screens.history.HistoryScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route
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
                HistoryScreen()
            }
        }
    }
}