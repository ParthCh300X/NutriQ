package parth.appdev.nutriq.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import parth.appdev.nutriq.presentation.common.UiState
import parth.appdev.nutriq.presentation.screens.home.HomeScreen
import parth.appdev.nutriq.presentation.screens.result.ResultScreen
import parth.appdev.nutriq.screens.history.HistoryScreen
import parth.appdev.nutriq.screens.result.ResultViewModel
import parth.appdev.nutriq.screens.scanner.ScannerScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

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
                    onScanClick = { navController.navigate(NavRoutes.Scanner.route) }
                )
            }

            composable(NavRoutes.Scanner.route) {
                ScannerScreen()
            }

            composable(NavRoutes.History.route) {
                HistoryScreen(
                    onItemClick = { barcode ->
                        navController.navigate(NavRoutes.Result.createRoute(barcode))
                    }
                )
            }

            composable(
                route = NavRoutes.Result.route,
                arguments = listOf(
                    navArgument("barcode") { type = NavType.StringType }
                )
            ) {
                val viewModel: ResultViewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()

                when (val s = state) {
                    is UiState.Success -> ResultScreen(food = s.data)
                    is UiState.Loading -> {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier.padding(padding),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                    else -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}