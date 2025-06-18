package com.example.randomcityapp.view.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.common.theme.contrastingTextColor
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.navigation.isLandscape
import com.example.randomcityapp.view.navigation.isTablet
import com.example.randomcityapp.view.viewmodel.CitiesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavHostController) {
    val citiesListViewModel: CitiesListViewModel = hiltViewModel()
    val cityState by citiesListViewModel.state.collectAsState()
    val isTwoPane = isTablet() && isLandscape()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBack = !isTwoPane && currentRoute?.startsWith("details") == true
    val toolbarTitle = when {
        cityState.selectedItem != null -> cityState.selectedItem?.cityName ?: "City Details"
        else -> "Random Cities"
    }
    val backgroundColor = cityState.selectedItem?.color
        ?.toColorOrDefault(MaterialTheme.colorScheme.primary)
        ?: MaterialTheme.colorScheme.primary
    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        toolbarTitle,
                        color = backgroundColor.contrastingTextColor()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    backgroundColor
                ),
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = {
                            citiesListViewModel.clearSelection()
                            navController.popBackStack()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = backgroundColor.contrastingTextColor()
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Reset DB") },
                            onClick = {
                                menuExpanded = false
                                citiesListViewModel.sendIntent(RandomCitiesIntent.ResetDb)
                            }
                        )
                        // Add more menu items here if needed
                    }
                }
            )
        }
    ) { innerPadding ->
        MainScreen(
            navController = navController,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            onBack = {
                citiesListViewModel.clearSelection()
                navController.popBackStack()
            },
            onTitleChange = { newTitle ->
                //toolbarTitle = newTitle
            }
        )
    }
}