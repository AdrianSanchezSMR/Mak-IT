package com.makit.tfg.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.makit.tfg.ui.MakItAppState
import com.makit.tfg.ui.components.BottomNavItem
import com.makit.tfg.ui.components.MakItBottomBar
import com.makit.tfg.ui.screens.CreateChallengeScreen
import com.makit.tfg.ui.screens.DashboardScreen
import com.makit.tfg.ui.screens.LoginScreen
import com.makit.tfg.ui.screens.ProfileScreen
import com.makit.tfg.ui.screens.StatsScreen
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val STATS = "stats"
    const val PROFILE = "profile"
    const val CREATE_CHALLENGE = "create_challenge"
}

@Composable
fun MakItNavHost(
    appState: MakItAppState = viewModel()
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backStack = navController.currentBackStackEntryAsState()
    val currentRoute = backStack.value?.destination?.route

    val showBottomBar = appState.isLoggedIn &&
        currentRoute in listOf(Routes.HOME, Routes.STATS, Routes.PROFILE)

    val selectedTab = when (currentRoute) {
        Routes.STATS -> BottomNavItem.Stats
        Routes.PROFILE -> BottomNavItem.Perfil
        else -> BottomNavItem.Inicio
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                MakItBottomBar(
                    selected = selectedTab,
                    onItemSelected = { item ->
                        val route = when (item) {
                            BottomNavItem.Inicio -> Routes.HOME
                            BottomNavItem.Stats -> Routes.STATS
                            BottomNavItem.Perfil -> Routes.PROFILE
                        }
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFabClick = { navController.navigate(Routes.CREATE_CHALLENGE) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLogin = { email, password ->
                        appState.login(email, password)
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onCreateAccount = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Registro disponible próximamente")
                        }
                    },
                    onForgotPassword = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Enlace de recuperación enviado (demo)")
                        }
                    }
                )
            }
            composable(Routes.HOME) {
                DashboardScreen(
                    profile = appState.profile,
                    todayChallenge = appState.todayChallenge,
                    onCompleteCheckIn = {
                        appState.completeCheckIn()
                        scope.launch {
                            snackbarHostState.showSnackbar("¡Check-in completado! 🎉")
                        }
                    },
                    onViewAllChallenges = {
                        navController.navigate(Routes.PROFILE)
                    }
                )
            }
            composable(Routes.STATS) {
                StatsScreen(profile = appState.profile)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    profile = appState.profile,
                    challenges = appState.challenges,
                    onViewAllChallenges = {},
                    onChangeReminder = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Selector de hora (demo)")
                        }
                    }
                )
            }
            composable(Routes.CREATE_CHALLENGE) {
                CreateChallengeScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { title, description, category, difficulty ->
                        appState.addChallenge(title, description, category, difficulty)
                        scope.launch {
                            snackbarHostState.showSnackbar("Reto guardado correctamente")
                        }
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
