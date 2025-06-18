package com.example.randomcityapp.view.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.randomcityapp.view.navigation.NavGraph
import com.example.randomcityapp.view.navigation.isLandscape
import com.example.randomcityapp.view.navigation.isTablet

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit
) {
    val tablet = isTablet()
    val landscape = isLandscape()
    val twoPane = tablet && landscape

    NavGraph(
        navController = navController,
        twoPane = twoPane,
        onBack = onBack,
        onTitleChange = onTitleChange
    )
}

