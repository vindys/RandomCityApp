package com.example.randomcityapp.view.list

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomcityapp.intent.RandomCitiesIntent
import com.example.randomcityapp.view.common.theme.toColorOrDefault
import com.example.randomcityapp.view.common.theme.toFormattedDateTime
import com.example.randomcityapp.view.main.CitiesListViewModel
import com.example.randomcityapp.view.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    modifier: Modifier = Modifier,
    onItemSelected: (Int, Context) -> Unit

) {
    val viewModel: CitiesListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val selectedCity = state.selectedItem

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Random Cities") })
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp)
        ) {
            items(state.dataList) { city ->
                val isSelected = city.id == selectedCity?.id

                // Apply selected background color or any other UI change
                val backgroundColor = if (isSelected) Color.Gray.copy(alpha = 0.2f) else Color.Transparent

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { onItemSelected(city.id, context) }
                        .background(backgroundColor)
                ) {
                    Text(
                        city.cityName, modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            .fillMaxWidth(), color = city.color.toColorOrDefault()
                    )
                    Text(city.time.toFormattedDateTime(),
                        modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
                }
            }
        }
    }
}
