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
import com.example.randomcityapp.view.viewmodel.CitiesListViewModel
import kotlinx.coroutines.flow.map

@Composable
fun NavGraph(
    navController: NavHostController,
    twoPane: Boolean,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit
) {
    val listViewModel: CitiesListViewModel = hiltViewModel()
    var selectedId by remember { mutableStateOf<Int?>(null) }
    val selectedItem by listViewModel.state
        .map { it.selectedItem }
        .collectAsState(initial = null)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    val isInDetails = currentBackStackEntry.value?.destination?.route?.startsWith("details") == true
    val state by listViewModel.state.collectAsState()

    LaunchedEffect(twoPane, isInDetails) {
        if (twoPane && isInDetails) {
            // Entering two-pane mode: go back to main to show list + details
            navController.navigate("main") {
                popUpTo("details") { inclusive = true }
            }
        } else if (!twoPane) {
            if (selectedItem != null && !isInDetails) {
                // In single-pane and a city is selected: navigate to details
                navController.navigate("details/${selectedItem!!.id}")
            } else if (selectedItem == null && isInDetails) {
                // No item selected but still in details screen, go back or clear state
                listViewModel.clearSelection()
                navController.popBackStack()
            }
        }
    }
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            if (twoPane) {
                Row(Modifier.fillMaxSize()) {
                    CitiesListScreen(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onItemSelected = { id, context ->
                            if (listViewModel.state.value.dataList.find { it.id == id } != null
                                && selectedId != id) {
                                listViewModel.sendIntent(RandomCitiesIntent.SelectItem(id))
                                /*selectedId = id

                                listViewModel.sendIntent(RandomCitiesIntent.SelectItem(id))*/
                                val city = state.dataList.find { it.id == id }
                                city?.let {
                                    onTitleChange(it.cityName) // Update title here
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
                        onBack
                    )
                }
            } else {
                // Single-pane UI shows only list, navigate to details via routes
                CitiesListScreen(
                    modifier = Modifier.fillMaxSize(),
                    onItemSelected = { id, context ->
                        listViewModel.sendIntent(RandomCitiesIntent.SelectItem(id))
                        navController.navigate("details/$id")
                        val city = state.dataList.find { it.id == id }
                        city?.let {
                            onTitleChange(it.cityName)
                            scheduleToastForCity(context, it.cityName)
                        }
                    }
                )
            }
        }

        composable("details/{cityId}") { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId")?.toIntOrNull()
            if (cityId != null) {
                backStackEntry.arguments?.getString("cityId")?.toIntOrNull()

                LaunchedEffect(selectedItem) {
                    selectedItem?.let {
                        onTitleChange(it.cityName)
                    }
                }
                CityDetailsScreen(
                    city = selectedItem,
                    modifier = Modifier.fillMaxSize(),
                    onBack
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