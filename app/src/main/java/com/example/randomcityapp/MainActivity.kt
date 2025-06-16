package com.example.randomcityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.randomcityapp.view.common.theme.RandomCityAppTheme
import com.example.randomcityapp.view.main.MainViewModel
import com.example.randomcityapp.view.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startGenerator(this@MainActivity)
            }
        }

        setContent {
            val navController = rememberNavController()
            RandomCityAppTheme {
                NavGraph(       //todo load navscreen from a composible function
                    navController = navController,
                    viewModel = viewModel,
                    onBack = { finish() }
                )
            }
        }
    }
}