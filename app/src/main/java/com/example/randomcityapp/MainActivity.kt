package com.example.randomcityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.randomcityapp.view.common.MainScreen
import com.example.randomcityapp.view.common.theme.RandomCityAppTheme
import com.example.randomcityapp.view.common.theme.contrastingTextColor
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.viewmodel.CitiesListViewModel
import com.example.randomcityapp.view.viewmodel.MainViewModel
import com.example.randomcityapp.view.navigation.isLandscape
import com.example.randomcityapp.view.navigation.isTablet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        // Keep splash screen until firstEmission is set
        splashScreen.setKeepOnScreenCondition {
            viewModel.firstEmission.value == null
        }
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startGenerator(this@MainActivity)
            }
        }

        setContent {
            RandomCityAppTheme {
                val navController = rememberNavController()
                AppScaffold(navController = navController)

            }
        }
    }
}

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
                }
            )
        }
    ) { innerPadding ->
        MainScreen(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
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



