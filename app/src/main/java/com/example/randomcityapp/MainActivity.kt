package com.example.randomcityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.common.AppScaffold
import com.example.randomcityapp.view.common.MainScreen
import com.example.randomcityapp.view.common.theme.RandomCityAppTheme
import com.example.randomcityapp.view.common.theme.contrastingTextColor
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.navigation.isLandscape
import com.example.randomcityapp.view.navigation.isTablet
import com.example.randomcityapp.view.viewmodel.CitiesListViewModel
import com.example.randomcityapp.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        // Start flow collection in lifecycle-aware manner
        viewModel.startGenerator(this)

        setContent {
            RandomCityAppTheme {
                val navController = rememberNavController()
                AppScaffold(navController = navController)

            }
        }
    }
}





