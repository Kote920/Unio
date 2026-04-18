package com.example.unio.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unio.domain.repository.AuthRepository
import com.example.unio.presentation.auth.AuthViewModel
import com.example.unio.presentation.auth.LoginScreen
import com.example.unio.presentation.auth.RegisterScreen
import com.example.unio.presentation.home.HomeScreen

@Composable
fun NavGraph(authRepository: AuthRepository? = null) {
    val navController = rememberNavController()
    val startDestination = if (authRepository?.isLoggedIn() == true) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            val viewModel = hiltViewModel<AuthViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            val viewModel = hiltViewModel<AuthViewModel>()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
