package com.example.randomcityapp.view.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.randomcityapp.domain.worker.scheduleToastForCity
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.detail.CityDetailsScreen
import com.example.randomcityapp.view.list.CitiesListScreen
import com.example.randomcityapp.view.main.CitiesListViewModel
import kotlinx.coroutines.flow.map

@Composable
fun NavGraph(
    navController: NavHostController,
    onBack: () -> Unit
) {
    val listViewModel: CitiesListViewModel = hiltViewModel()
    val tablet = isTablet()
    val landscape = isLandscape()
    val twoPane = tablet && landscape
    var selectedId by remember { mutableStateOf<Int?>(null) }
    val selectedItem by listViewModel.state
        .map { it.selectedItem }
        .collectAsState(initial = null)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    val isInDetails = currentBackStackEntry.value?.destination?.route?.startsWith("details") == true

    LaunchedEffect(twoPane, isInDetails) {
        if (twoPane && isInDetails) {
            // Two-pane mode should show both list and details in one screen
            navController.navigate("main") {
                popUpTo("details") { inclusive = true }
            }
        } else if (!twoPane && !isInDetails && selectedId != null) {
            // Switch to single-pane details if needed
            navController.navigate("details/$selectedId")
        }
    }
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val state by listViewModel.state.collectAsState()
            if (twoPane) {
                Row(Modifier.fillMaxSize()) {
                    CitiesListScreen(
                        modifier = Modifier
                            .weight(1f)         // This makes it fill 50% of width
                            .fillMaxHeight(),    // Fill height inside Row
                        onItemSelected = { id, context ->
                            if (listViewModel.state.value.dataList.find { it.id == id } != null && selectedId != id) {
                                listViewModel.selectCity(id)
                                listViewModel.sendIntent(RandomCitiesIntent.SelectItem(id))
                                selectedId = id
                                val city = state.dataList.find { it.id == id }
                                city?.let {
                                    scheduleToastForCity(context, it.cityName)
                                }
                            }
                        }
                    )
                    CityDetailsScreen(
                        city = selectedItem,
                        modifier = Modifier
                            .weight(1f)         // Fill other half
                            .fillMaxHeight(),
                        onBack = onBack,
                        showBack = false
                    )
                }
            } else {
                // Single-pane UI shows only list, navigate to details via routes
                CitiesListScreen(
                    modifier = Modifier.fillMaxSize(),
                    onItemSelected = { id, context ->
                        listViewModel.selectCity(id)
                        navController.navigate("details/$id")
                        val city = state.dataList.find { it.id == id }
                        city?.let {
                            scheduleToastForCity(context, it.cityName)
                        }
                    }
                )
            }
        }
        composable("details/{cityId}") { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId")?.toIntOrNull()
            if (cityId != null) {
                CityDetailsScreen(
                    city = selectedItem,
                    modifier = Modifier.fillMaxSize(),
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
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