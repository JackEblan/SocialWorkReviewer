/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.eblan.socialworkreviewer.feature.question.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlin.math.roundToInt

@Composable
internal fun ScoreScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    score: Int,
    questions: List<Question>,
    minutes: String?,
    onShowCorrectChoices: (questions: List<Question>) -> Unit,
    onQuitQuestions: () -> Unit,
) {
    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    val average = (score.toDouble() / questions.size.toDouble()) * 100.0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onShowCorrectChoices(questions)
                },
            ) {
                Icon(
                    imageVector = Swr.Eye,
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
            if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Statistics(
                        average = average,
                        score = score,
                        questions = questions,
                        minutes = minutes,
                    )
                }
            } else {
                Row(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Statistics(
                        average = average,
                        score = score,
                        questions = questions,
                        minutes = minutes,
                    )
                }
            }
        }
    }

    if (showQuitAlertDialog) {
        QuitAlertDialog(
            onDismissRequest = {
                showQuitAlertDialog = false
            },
            onConfirmation = {
                showQuitAlertDialog = false
                onQuitQuestions()
            },
            dialogTitle = "Quit Questions",
            dialogText = "Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }
}

@Composable
private fun Statistics(
    average: Double,
    score: Int,
    questions: List<Question>,
    minutes: String?,
) {
    AverageCircularProgressIndicator(
        progress = { (average / 100).toFloat() },
        modifier = Modifier.size(150.dp),
        strokeWidth = 8.dp,
        strokeCap = StrokeCap.Round,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = "${average.roundToInt()}%",
            style = MaterialTheme.typography.titleLarge.copy(
                brush = Brush.linearGradient(
                    colors = LocalGradientColors.current.topBarTitleColorsDefault,
                ),
            ),
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    StatisticsText(title = "Correct", subtitle = "$score")

    Spacer(modifier = Modifier.height(20.dp))

    StatisticsText(title = "Wrong", subtitle = "${questions.size - score}")

    Spacer(modifier = Modifier.height(20.dp))

    StatisticsText(
        title = "Time",
        subtitle = if (minutes.isNullOrBlank()) "Time's up!" else minutes,
    )
}

@Composable
private fun StatisticsText(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.headlineLarge.copy(
                brush = Brush.linearGradient(
                    colors = LocalGradientColors.current.topBarTitleColorsDefault,
                ),
            ),
        )
    }
}
