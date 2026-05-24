package com.makit.tfg.navigation

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.makit.tfg.data.MakItRepository
import com.makit.tfg.ui.MakItAppState
import com.makit.tfg.ui.MakItViewModelFactory
import com.makit.tfg.ui.components.BottomNavItem
import com.makit.tfg.ui.components.MakItBottomBar
import com.makit.tfg.ui.screens.CreateChallengeScreen
import com.makit.tfg.ui.screens.DashboardScreen
import com.makit.tfg.ui.screens.InterestsScreen
import com.makit.tfg.ui.screens.LoginScreen
import com.makit.tfg.ui.screens.ProfileScreen
import com.makit.tfg.ui.screens.RegisterScreen
import com.makit.tfg.ui.screens.StatsScreen
import com.makit.tfg.ui.theme.MakGreen
import kotlinx.coroutines.launch
import java.util.Calendar

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val STATS = "stats"
    const val PROFILE = "profile"
    const val INTERESTS = "interests"
    const val CREATE_CHALLENGE = "create_challenge"
}

@Composable
fun MakItNavHost(
    repository: MakItRepository,
    appState: MakItAppState = viewModel(factory = MakItViewModelFactory(repository))
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val backStack = navController.currentBackStackEntryAsState()
    val currentRoute = backStack.value?.destination?.route

    LaunchedEffect(appState.errorMessage) {
        appState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            appState.clearError()
        }
    }

    LaunchedEffect(appState.isLoggedIn, appState.isRestoringSession) {
        if (!appState.isRestoringSession) {
            if (appState.isLoggedIn && currentRoute == Routes.LOGIN) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            if (!appState.isLoggedIn && currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    val showBottomBar = appState.isLoggedIn &&
        currentRoute in listOf(Routes.HOME, Routes.STATS, Routes.PROFILE)

    val selectedTab = when (currentRoute) {
        Routes.STATS -> BottomNavItem.Stats
        Routes.PROFILE -> BottomNavItem.Perfil
        else -> BottomNavItem.Inicio
    }

    if (appState.isRestoringSession) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MakGreen)
        }
        return
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
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
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
                    isLoading = appState.isLoading,
                    onLogin = { username, password ->
                        appState.login(username, password) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    },
                    onCreateAccount = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    isLoading = appState.isLoading,
                    onBack = { navController.popBackStack() },
                    onRegister = { username, email, password ->
                        appState.register(username, email, password) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(Routes.HOME) {
                DashboardScreen(
                    profile = appState.profile,
                    todayChallenges = appState.todayChallenges,
                    isLoading = appState.isLoading,
                    onCompleteCheckIn = { challenge ->
                        appState.completeCheckIn(challenge) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Check-in completado")
                            }
                        }
                    },
                    onViewAllChallenges = {
                        navController.navigate(Routes.PROFILE) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.STATS) {
                StatsScreen(
                    profile = appState.profile,
                    weeklyProgress = appState.weeklyProgress
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    profile = appState.profile,
                    challenges = appState.myChallenges.ifEmpty { appState.catalogChallenges },
                    onViewAllChallenges = {},
                    onEditInterests = { navController.navigate(Routes.INTERESTS) },
                    onChangeReminder = {
                        val cal = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val formatted = "%02d:%02d".format(hour, minute)
                                appState.updateReminderHour(formatted) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Hora de aviso actualizada")
                                    }
                                }
                            },
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    onLogout = {
                        appState.logout {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onCreateReto = { navController.navigate(Routes.CREATE_CHALLENGE) }
                )
            }
            composable(Routes.INTERESTS) {
                InterestsScreen(
                    categories = appState.categories,
                    selectedIds = appState.selectedInterestIds,
                    isLoading = appState.isLoading,
                    onBack = { navController.popBackStack() },
                    onSave = { ids ->
                        appState.updateInterests(ids) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Intereses guardados")
                            }
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
            composable(Routes.CREATE_CHALLENGE) {
                CreateChallengeScreen(
                        categories = appState.categories,
                        isLoading = appState.isLoading,
                        onBack = { navController.popBackStack() },
                        onSave = { categoriaId, title, description ->
                            appState.createCatalogReto(categoriaId, title, description) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Reto creado")
                                }
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.HOME) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
            }
        }
    }
}
