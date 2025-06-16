package com.example.randomcityapp.view.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.randomcityapp.domain.worker.scheduleToastForCity
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.common.theme.SplashScreen
import com.example.randomcityapp.view.detail.CityDetailsScreen
import com.example.randomcityapp.view.list.CitiesListScreen
import com.example.randomcityapp.view.main.MainViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val tablet = isTablet()
    val landscape = isLandscape()
    val twoPane = tablet && landscape

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(twoPane, currentBackStackEntry.value?.destination?.route) {
        if (twoPane && currentBackStackEntry.value?.destination?.route == "details") {
            // Switch to two-pane layout when conditions are met
            navController.navigate("main") {
                popUpTo("details") { inclusive = true }
            }
        }
    }
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            if (twoPane) {
                Row(Modifier.fillMaxSize()) {
                    CitiesListScreen(
                        modifier = Modifier
                            .weight(1f)         // This makes it fill 50% of width
                            .fillMaxHeight(),    // Fill height inside Row
                        viewModel = viewModel,
                        onItemSelected = { id, context ->
                            if(viewModel.selectedCityId.value == null || viewModel.selectedItem.value?.id != id) {
                                viewModel.selectCity(id)
                                viewModel.sendIntent(RandomCitiesIntent.SelectItem(id))

                                val city = state.dataList.find { it.id == id }
                                city?.let {
                                    scheduleToastForCity(context, it.cityName)
                                }
                            }
                        }
                    )
                    CityDetailsScreen(
                        modifier = Modifier
                            .weight(1f)         // Fill other half
                            .fillMaxHeight(),
                        viewModel = viewModel,
                        onBack = onBack,
                        showBack = false
                    )
                }
            } else {
                // Single-pane UI shows only list, navigate to details via routes
                CitiesListScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    onItemSelected = { id, context ->
                        viewModel.selectCity(id)
                        navController.navigate("details")
                        val city = state.dataList.find { it.id == id }
                        city?.let {
                            scheduleToastForCity(context, it.cityName)
                        }
                    }
                )
            }
        }
        composable("details") {
            CityDetailsScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}


@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.smallestScreenWidthDp >= 600
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
}