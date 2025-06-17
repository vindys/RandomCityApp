package com.example.randomcityapp.view.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.randomcityapp.intent.CitiesState
import com.example.randomcityapp.model.source.local.RandomCity
import com.example.randomcityapp.view.common.theme.RandomCityAppTheme
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.common.theme.toFormattedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    modifier: Modifier = Modifier,
    state: CitiesState,
    onItemSelected: (Int) -> Unit
) {
    val selectedCity = state.selectedItem

    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        items(state.dataList) { city ->
            val isSelected = city.id == selectedCity?.id

            // Apply selected background color or any other UI change
            val backgroundColor =
                if (isSelected) Color.Gray.copy(alpha = 0.2f) else Color.Transparent

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onItemSelected(city.id) }
                    .background(backgroundColor)
            ) {
                Text(
                    city.cityName, modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth(), color = city.color.toColorOrDefault()
                )
                Text(
                    city.time.toFormattedDateTime(),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCitiesListScreen() {
    val sampleCities = listOf(
        RandomCity(
            id = 1,
            cityName = "New York",
            color = "YELLOW",
            time = System.currentTimeMillis(),
            lat = 0.0,
            lng = 0.0
        ),
        RandomCity(
            id = 2,
            cityName = "Tokyo",
            color = "#33C1FF",
            time = System.currentTimeMillis(),
            lat = 0.0,
            lng = 0.0
        ),
        RandomCity(
            id = 3,
            cityName = "Berlin",
            color = "RED",
            time = System.currentTimeMillis(),
            lat = 0.0,
            lng = 0.0
        ),
    )

    val previewState = CitiesState(
        dataList = sampleCities,
        selectedItem = sampleCities[1]
    )

    RandomCityAppTheme {
        CitiesListScreen(
            state = previewState,
            onItemSelected = {} // No-op for preview
        )
    }
}

