package com.example.randomcityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.randomcityapp.view.common.AppScaffold
import com.example.randomcityapp.view.common.theme.RandomCityAppTheme
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





