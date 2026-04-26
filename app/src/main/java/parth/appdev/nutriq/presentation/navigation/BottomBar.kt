package parth.appdev.nutriq.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        NavRoutes.Home to Icons.Default.Home,
        NavRoutes.Scanner to Icons.Default.CameraAlt,
        NavRoutes.History to Icons.Default.List
    )

    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()

        items.forEach { (route, icon) ->
            val selected = backStackEntry.value?.destination?.route == route.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(NavRoutes.Home.route)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(icon, contentDescription = route.route) },
                label = {
                    Text(route.route.replaceFirstChar { it.uppercase() })
                }
            )
        }
    }
}