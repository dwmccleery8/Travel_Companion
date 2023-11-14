package com.example.travelcompanion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelcompanion.Screens.MapScreen
import com.example.travelcompanion.Screens.ResultsScreen
import com.example.travelcompanion.Screens.UserInfoScreen


sealed class NavScreens(val route: String) {
    object MapScreen : NavScreens(route = "MapScreen")
    object UserInfoScreen: NavScreens(route = "UserInfoScreen")
    object ResultsScreen: NavScreens(route = "ResultsScreen")
}

@Composable
fun TravelCompanionApp(
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = NavScreens.UserInfoScreen.route) {

        //first screen, user info
        composable(route = NavScreens.UserInfoScreen.route) {
            UserInfoScreen(
                onNext = { navController.navigate(NavScreens.MapScreen.route)}
            )
        }
        //second screen, map
        composable(route = NavScreens.MapScreen.route) {
            MapScreen(
                onNext = { navController.navigate(NavScreens.ResultsScreen.route)}
            )

        }

        //third screen, results
        composable(route = NavScreens.ResultsScreen.route) {
            ResultsScreen(
                onNext = { navController.popBackStack(route = NavScreens.UserInfoScreen.route, inclusive = false)}
            )
        }
    }

}