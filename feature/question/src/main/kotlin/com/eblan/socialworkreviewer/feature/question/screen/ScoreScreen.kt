package com.eblan.socialworkreviewer.feature.question.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Question

@Composable
internal fun ScoreScreen(
    modifier: Modifier = Modifier,
    score: Int,
    questions: List<Question>,
    minutes: String?,
    onShowCorrectChoices: (questions: List<Question>) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onShowCorrectChoices(questions)
                },
            ) {
                Icon(
                    imageVector = Swr.Check,
                    contentDescription = "",
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$score/${questions.size}",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        brush = Brush.linearGradient(
                            colors = LocalGradientColors.current.topBarTitleColorsDefault,
                        ),
                    ),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Time",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = minutes ?: "Time's up!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        brush = Brush.linearGradient(
                            colors = LocalGradientColors.current.topBarTitleColorsDefault,
                        ),
                    ),
                )
            }
        }

    }
}