package com.mahmoudibrahem.taskii.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mahmoudibrahem.taskii.navigation.screens.OnboardingScreens
import com.mahmoudibrahem.taskii.ui.screens.onboarding.OnBoardingScreen
import com.mahmoudibrahem.taskii.ui.screens.user_naming.UserNamingScreen
import com.mahmoudibrahem.taskii.util.Constants.BOTTOM_BAR_GRAPH_ROUTE
import com.mahmoudibrahem.taskii.util.Constants.ONBOARDING_GRAPH_ROUTE


fun NavGraphBuilder.onboardingNavGraph(navController: NavHostController) {
    navigation(
        route = ONBOARDING_GRAPH_ROUTE,
        startDestination = OnboardingScreens.OnboardingScreen.route
    ) {

        composable(route = OnboardingScreens.OnboardingScreen.route) { _ ->
            OnBoardingScreen(
                onNavigateToNameScreen = {
                    navController.navigate(
                        route = OnboardingScreens.NamingUserScreen.route
                    ) {
                        popUpTo(OnboardingScreens.OnboardingScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = OnboardingScreens.NamingUserScreen.route) { _ ->
            UserNamingScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(
                        route = BOTTOM_BAR_GRAPH_ROUTE
                    ) {
                        popUpTo(route = ONBOARDING_GRAPH_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }
}