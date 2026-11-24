package com.example.navigationlab.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.navigationlab.Routes
import com.example.navigationlab.data.remote.RetrofitClient
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.repository.DefaultStudentRepository
import com.example.navigationlab.ui.detail.DetailViewModel
import com.example.navigationlab.ui.home.HomeViewModel
import com.example.navigationlab.ui.screen.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.local.DatabaseProvider
import com.example.navigationlab.data.repository.FavoriteRepository
import com.example.navigationlab.ui.favorites.FavoritesViewModel
import com.google.android.gms.location.LocationServices
import com.example.navigationlab.data.location.DefaultLocationRepository
import com.example.navigationlab.ui.location.LocationScreen
import com.example.navigationlab.ui.location.LocationViewModel

@Composable
fun NavigationLabApp() {
    val remoteDataSource = remember {
        StudentRemoteDataSource(RetrofitClient.apiService)
    }
    val repository = remember {
        DefaultStudentRepository(remoteDataSource)
    }
    val context = LocalContext.current
    val preferencesRepository = remember {
        UserPreferencesRepository(context.applicationContext)
    }
    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context)
                .favoriteStudentDao()
        )
    }
    val navController = rememberNavController()
    val applicationContext = LocalContext.current.applicationContext
    val locationClient = remember(applicationContext) {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }
    val locationRepository = remember(locationClient) {
        DefaultLocationRepository(
            context = applicationContext, client = locationClient
        )
    }
    val locationViewModelFactory = remember(locationRepository) {
        viewModelFactory {
            initializer {
                LocationViewModel(locationRepository)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository, preferencesRepository)
            )
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = homeUiState,
                searchQuery = searchQuery,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onRetry = homeViewModel::retry,
                onRefresh = homeViewModel::refresh,
                onOpenFavorites = { navController.navigate(Routes.FAVORITES) },
                onOpenLocation = { navController.navigate(Routes.LOCATION) }
            )
        }
        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(
                    favoriteRepository,
                    preferencesRepository
                )
            )
            val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = favoritesUiState,
                onSortAscendingChange = favoritesViewModel::onSortAscendingChange,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }
            DetailScreen(
                uiState = detailUiState,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.LOCATION) {
            val locationViewModel: LocationViewModel = viewModel(
                factory = locationViewModelFactory
            )
            LocationScreen(
                viewModel = locationViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}