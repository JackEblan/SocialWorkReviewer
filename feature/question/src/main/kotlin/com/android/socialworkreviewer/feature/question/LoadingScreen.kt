package com.android.socialworkreviewer.feature.question

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerLoadingWheel


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    SocialWorkReviewerLoadingWheel(
        modifier = modifier,
        contentDescription = "SocialWorkReviewerLoadingWheel",
    )
}