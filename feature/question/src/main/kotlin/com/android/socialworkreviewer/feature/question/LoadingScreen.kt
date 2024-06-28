package com.android.socialworkreviewer.feature.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerLoadingWheel


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Scaffold { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            SocialWorkReviewerLoadingWheel(
                modifier = Modifier.align(Alignment.Center),
                contentDescription = "SocialWorkReviewerLoadingWheel"
            )
        }
    }
}