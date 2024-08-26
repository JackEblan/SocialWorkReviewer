package com.eblan.socialworkreviewer.feature.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eblan.socialworkreviewer.feature.about.AboutScreen

fun NavController.navigateToAboutScreen() {
    navigate(AboutRouteData) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.aboutScreen() {
    composable<AboutRouteData> {
        AboutScreen()
    }
}