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
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.local.DatabaseProvider
import com.example.navigationlab.data.repository.FavoriteRepository

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
                onRefresh = homeViewModel::refresh
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
    }
}