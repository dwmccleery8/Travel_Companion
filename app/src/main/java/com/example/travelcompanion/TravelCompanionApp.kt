package com.example.travelcompanion

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelcompanion.models.DirectionsViewModel
import com.example.travelcompanion.models.GeocodingViewModel
import com.example.travelcompanion.screens.AddressAutoCompleteScreen
import com.example.travelcompanion.screens.UserInfoScreen
import com.example.travelcompanion.screens.TitleScreen
import com.example.travelcompanion.models.OpenAiVM
import com.example.travelcompanion.models.WeatherViewModel
import com.example.travelcompanion.screens.WeatherScreen
import java.time.temporal.WeekFields


sealed class NavScreens(val route: String) {
    object TitleScreen: NavScreens(route = "TitleScreen")
    object MapScreen : NavScreens(route = "MapScreen")
    object UserInfoScreen: NavScreens(route = "UserInfoScreen")
    object ResultsScreen: NavScreens(route = "ResultsScreen")

    object WeatherScreen: NavScreens(route = "WeatherScreen")

    object AddressAutoCompleteScreen : NavScreens(route = "AddressAutoCompleteScreen")
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TravelCompanionApp(
    context: Context,
    intentOnClick: (Double, Double)-> Unit,
    navController: NavHostController = rememberNavController(),
    openAiVM : OpenAiVM = viewModel()

) {
    val directionsVM = viewModel<DirectionsViewModel>()
    val weatherVM = viewModel<WeatherViewModel>()
    val addressVM = viewModel<GeocodingViewModel>()


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
                onNext = { navController.navigate(NavScreens.AddressAutoCompleteScreen.route)}
            )
        }
        //second screen, map
        composable(route = NavScreens.AddressAutoCompleteScreen.route) {
            AddressAutoCompleteScreen(onNext = {
                navController.navigate(NavScreens.WeatherScreen.route)
            }, directionsVM = directionsVM, weatherVM = weatherVM, addressVM = addressVM)
        }

        //third screen, results
        composable(route = NavScreens.WeatherScreen.route) {

            WeatherScreen(
                onNext = {
                    navController.popBackStack(route = NavScreens.TitleScreen.route, inclusive = false)
                    openAiVM.reset()
                    directionsVM.reset()
                    addressVM.reset()
                    weatherVM.reset()
                },
                openAiVM = openAiVM,
                weatherVM = weatherVM,
                directionsVM = directionsVM,
                addressVM = addressVM,
                intentOnClick = { lat, long ->
                    intentOnClick(lat, long)
                })

        }
    }

}