# Random City App

A modern Android app built using **Jetpack Compose**, **Hilt**, **Room**, and **WorkManager**. The app continuously generates random cities and displays them in a list, allowing users to view city details in a responsive single-pane or two-pane layout (tablet/landscape support). Includes Google Maps integration and supports database reset.

---

## Features

-  Random city generation in the background using Kotlin Flows  
-  Live city list using Room + Flow  
-  Single-pane &  Two-pane responsive UI (tablet & landscape support)  
-  City detail view with dynamic color and Google Maps integration  
-  ViewModel-driven state with Hilt DI  
-  Manual reset option to clear the city database  
-  Material 3 theming with Compose UI  
-  Seamless orientation and lifecycle handling  

---

##  Screenshots

| Phone (Portrait) | Tablet (Two Pane) |
|------------------|-------------------|
| ![Phone](/screenshots/phone_portrait.png) | ![Tablet](/screenshots/tablet_twopane.png) |

---

##  Built With

- **MVI**  
- **Jetpack Compose**  
- **Hilt (DI)**  
- **Room (Persistence)**  
- **Kotlin Coroutines & Flow**  
- **WorkManager** (Optional for background generation)  
- **Navigation Compose**  
- **Google Maps SDK**

---

##  Project Structure
```text

├── app
│   ├── BaseApplication.kt
│   ├── MainActivity.kt
│   ├── di # Hilt module
│   │   └── ModelModule.kt
│   ├── domain # Use case files - City Producer, Geocoding service for getting latlon, Worker
│   │   ├── producer
│   │   │   ├── AndroidGeocodingService.kt
│   │   │   ├── GeocodingService.kt
│   │   │   └── RandomCityProducer.kt
│   │   └── worker
│   │       ├── ToastWorker.kt
│   │       └── WorkUtils.kt
│   ├── intent # Intent and state for MVI structure 
│   │   ├── CitiesState.kt
│   │   └── RandomCitiesIntent.kt
│   ├── model # Model and repository
│   │   ├── repository
│   │   │   ├── CitiesRepoImpl.kt
│   │   │   └── CitiesRepository.kt
│   │   └── source
│   │       └── local
│   │           ├── RandomCity.kt
│   │           ├── RandomCityDao.kt
│   │           └── RandomCityDatabase.kt
│   └── view
│       ├── common # Common view and theme
│       │   ├── AppScaffold.kt
│       │   ├── MainScreen.kt
│       │   └── theme
│       │       ├── Color.kt
│       │       ├── ColorExtensions.kt
│       │       ├── DateTimeExtensions.kt
│       │       ├── Theme.kt
│       │       └── Type.kt
│       ├── detail
│       │   └── CityDetailsScreen.kt
│       ├── list
│       │   └── CitiesListScreen.kt
│       ├── navigation
│       │   └── AppNavController.kt
│       └── viewmodel # ViewModels (Main, List, Details)
│           ├── CitiesListViewModel.kt
│           ├── CityDetailsViewModel.kt
│           └── MainViewModel.kt

```
---

##  Getting Started

-  Setup API Key  
    Add your Maps API Key to `local.properties`:

    MAPS_API_KEY=YOUR_API_KEY_HERE

---

## Previews


Compose previews are available for individual components like city list and detail screens.

---

📝 License
This project is licensed under the MIT License. See the LICENSE file for details.
