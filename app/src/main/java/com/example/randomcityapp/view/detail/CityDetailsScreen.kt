package com.example.randomcityapp.view.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomcityapp.model.source.local.RandomCity
import com.example.randomcityapp.view.common.theme.contrastingTextColor
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.main.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onBack: () -> Unit,
    showBack: Boolean = true
) {
    val cityState by viewModel.selectedItem.collectAsState()
    val city = cityState

    if (city == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No city selected")
        }
        return
    }
    val fallbackColor = MaterialTheme.colorScheme.primary

    val toolbarColor = remember(city.color) {
        city.color.toColorOrDefault(fallbackColor)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        city.cityName,
                        color = city.color.toColorOrDefault().contrastingTextColor()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = toolbarColor
                ),
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = city.color.toColorOrDefault().contrastingTextColor()
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            CityMapSection(city = city)
        }
    }

}

@Composable
fun CityMapSection(city: RandomCity) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(city.lat, city.lng), 10f
        )
    }

    // 🔄 Re-focus map camera when city changes
    LaunchedEffect(city.lat, city.lng) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(city.lat, city.lng), 10f
            )
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(city.lat, city.lng)),
            title = city.cityName
        )
    }
}
