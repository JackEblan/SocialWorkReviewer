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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog

@Composable
internal fun CorrectChoicesScreen(
    modifier: Modifier = Modifier,
    questions: List<Question>,
    answeredQuestions: Map<Question, List<String>>,
    lastCountDownTime: String?,
    score: Int,
    onQuitQuestions: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            answeredQuestions.size
        },
    )

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / answeredQuestions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            CorrectChoicesTopBar(
                wrongText = "${questions.size - score}",
                correctText = "$score",
                lastCountDownTime = lastCountDownTime,
            )

            SwrLinearProgressIndicator(
                progress = {
                    animatedProgress
                },
                modifier = modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(10.dp),
                strokeCap = StrokeCap.Round,
            )

            HorizontalPager(state = pagerState) { page ->
                CorrectChoicesPage(
                    page = page,
                    questions = questions,
                    answeredQuestions = answeredQuestions,
                )
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
private fun CorrectChoicesTopBar(
    modifier: Modifier = Modifier,
    wrongText: String,
    correctText: String,
    lastCountDownTime: String?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Swr.Close, contentDescription = "Wrong")

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = wrongText, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(20.dp))

        Icon(imageVector = Swr.Check, contentDescription = "Correct")

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = correctText, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(20.dp))

        Icon(imageVector = Swr.AccessTime, contentDescription = "Minutes")

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = if (lastCountDownTime.isNullOrBlank()) "Time's up!" else lastCountDownTime,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun CorrectChoicesPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    questions: List<Question>,
    answeredQuestions: Map<Question, List<String>>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        Spacer(modifier = Modifier.height(10.dp))

        CorrectChoicesSelection(
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            selectedChoices = answeredQuestions[questions[page]],
        )
    }
}

@Composable
private fun CorrectChoicesSelection(
    modifier: Modifier = Modifier,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>?,
) {
    val greenGradientColors = listOf(
        Color(0xFF43A047),
        Color(0xFF7CB342),
        Color(0xFF039BE5),
    )

    val redGradientColors = listOf(Color.Red, Color.Blue, Color.Red)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
    ) {
        choices.forEach { choice ->
            val correctChoice = choice in correctChoices

            val wrongChoice = choice !in correctChoices && selectedChoices?.contains(choice) == true

            val choiceBorderGradientColors = if (correctChoice) {
                greenGradientColors
            } else if (wrongChoice) {
                redGradientColors
            } else {
                emptyList()
            }

            val choiceBrush = if (choiceBorderGradientColors.isNotEmpty()) {
                Brush.linearGradient(
                    colors = choiceBorderGradientColors,
                )
            } else {
                CardDefaults.outlinedCardBorder().brush
            }

            OutlinedCard(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    width = if (correctChoice || wrongChoice) 2.dp else 1.dp,
                    brush = choiceBrush,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    Text(
                        text = choice,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
