package com.example.randomcityapp.view.common.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.main.MainViewModel

@Composable
fun SplashScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    val firstCity by viewModel.firstEmission.collectAsState()

    LaunchedEffect(firstCity) {
        if (firstCity == null) {
            viewModel.sendIntent(RandomCitiesIntent.ResetDb)
        }
        else {
            onNavigate()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Loading Random City...", style = MaterialTheme.typography.headlineMedium)
    }
}
