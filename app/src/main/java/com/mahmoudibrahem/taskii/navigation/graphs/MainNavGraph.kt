package com.mahmoudibrahem.taskii.navigation.graphs

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mahmoudibrahem.taskii.navigation.screens.HomeScreens
import com.mahmoudibrahem.taskii.ui.screens.analytics.AnalyticsScreen
import com.mahmoudibrahem.taskii.ui.screens.create_task.CreateTaskScreen
import com.mahmoudibrahem.taskii.ui.screens.home.HomeScreen
import com.mahmoudibrahem.taskii.ui.screens.search.SearchScreen
import com.mahmoudibrahem.taskii.ui.screens.task_details.TaskDetailsScreen
import com.mahmoudibrahem.taskii.util.Constants.BOTTOM_BAR_GRAPH_ROUTE

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        route = BOTTOM_BAR_GRAPH_ROUTE,
        startDestination = HomeScreens.Home.route
    ) {

        composable(route = HomeScreens.Home.route) {
            HomeScreen(
                onNavigateToCreateTask = {
                    navController.navigate(
                        route = HomeScreens.CreateTask.route.replace(
                            "{task_id}",
                            "-1"
                        )
                    )
                },
                onNavigateToAnalytics = { navController.navigate(route = HomeScreens.Analytics.route) },
                onNavigateToSearch = { navController.navigate(route = HomeScreens.Search.route) },
                onNavigateToTaskDetails = {
                    navController.navigate(
                        route = HomeScreens.TaskDetails.route.replace(
                            "{task_id}",
                            it.id.toString()
                        )
                    )
                }
            )
        }

        composable(route = HomeScreens.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToTaskDetails = {
                    navController.navigate(
                        route = HomeScreens.TaskDetails.route.replace(
                            "{task_id}",
                            it.id.toString()
                        )
                    )
                }
            )
        }

        composable(route = HomeScreens.CreateTask.route) { backStackEntry ->
            Log.d("```TAG```", "mainNavGraph: ${backStackEntry.arguments?.getString("task_id")}")
            val taskId = backStackEntry.arguments?.getString("task_id")?.toInt()
            if (taskId != null) {
                CreateTaskScreen(
                    taskId = taskId,
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable(route = HomeScreens.TaskDetails.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("task_id")?.toInt()
            if (taskId != null) {
                TaskDetailsScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToEditTask = {
                        navController.navigate(
                            HomeScreens.CreateTask.route.replace("{task_id}", taskId.toString())
                        )
                    }
                )
            }
        }

        composable(route = HomeScreens.Analytics.route) {
            AnalyticsScreen(navController = navController)
        }

    }
}