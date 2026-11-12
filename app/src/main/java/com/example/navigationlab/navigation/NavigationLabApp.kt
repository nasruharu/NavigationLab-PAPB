package com.example.navigationlab.navigation

import androidx.compose.runtime.*
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

@Composable
fun NavigationLabApp() {
    val remoteDataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository = remember { DefaultStudentRepository(remoteDataSource) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository)
            )
            HomeScreen(
                uiState = homeViewModel.uiState,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onRetry = homeViewModel::retry,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onSearch = homeViewModel::searchStudents
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository)
            )
            LaunchedEffect(studentId) {
                viewModel.loadStudent(studentId)
            }
            DetailScreen(
                uiState = viewModel.uiState,
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