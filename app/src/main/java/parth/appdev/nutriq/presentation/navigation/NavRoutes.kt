package parth.appdev.nutriq.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Scanner : NavRoutes("scanner")
    object History : NavRoutes("history")
}