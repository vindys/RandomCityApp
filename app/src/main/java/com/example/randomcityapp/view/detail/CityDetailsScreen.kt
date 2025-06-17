package com.example.randomcityapp.view.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomcityapp.model.source.local.RandomCity
import com.example.randomcityapp.view.viewmodel.CityDetailsViewModel
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
    city: RandomCity?,
    modifier: Modifier = Modifier
) {
    if (city == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No city selected")
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CityMapSection(city = city)
    }
}

@Composable
fun CityMapSection(city: RandomCity) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(city.lat, city.lng), 10f
        )
    }

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
