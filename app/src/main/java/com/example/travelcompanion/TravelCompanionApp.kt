package com.example.travelcompanion

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelcompanion.screens.MapScreen
import com.example.travelcompanion.screens.UserInfoScreen
import com.example.travelcompanion.screens.TitleScreen
import com.example.travelcompanion.models.OpenAiVM
import com.example.travelcompanion.screens.WeatherScreen


sealed class NavScreens(val route: String) {
    object TitleScreen: NavScreens(route = "TitleScreen")
    object MapScreen : NavScreens(route = "MapScreen")
    object UserInfoScreen: NavScreens(route = "UserInfoScreen")
    object ResultsScreen: NavScreens(route = "ResultsScreen")
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TravelCompanionApp(
    context: Context,
    intentOnClick: (Double, Double) -> Unit,
    navController: NavHostController = rememberNavController(),
    openAiVM: OpenAiVM = viewModel()

) {


    NavHost(navController = navController, startDestination = NavScreens.TitleScreen.route) {

        //Title screen
        composable(route = NavScreens.TitleScreen.route){
            TitleScreen(
                onNext = { navController.navigate(NavScreens.UserInfoScreen.route)}
            )
        }
        //first screen, user info
        composable(route = NavScreens.UserInfoScreen.route) {
            UserInfoScreen(
                openAiVM,
                context,
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
            WeatherScreen(
                onNext = {
                    navController.popBackStack(route = NavScreens.TitleScreen.route, inclusive = false)
                    openAiVM.reset()
                },
                openAiVM = openAiVM,
                intentOnClick = {lat, long ->
                    intentOnClick(lat,long)
                }
            )
        }
    }

}