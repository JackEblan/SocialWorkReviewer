package com.android.socialworkreviewer.feature.question.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.android.socialworkreviewer.feature.question.QuestionRoute

fun NavController.navigateToQuestionScreen(id: String) {
    navigate(QuestionRouteData(id = id))
}

fun NavGraphBuilder.questionScreen() {
    composable<QuestionRouteData> {
        QuestionRoute()
    }
}