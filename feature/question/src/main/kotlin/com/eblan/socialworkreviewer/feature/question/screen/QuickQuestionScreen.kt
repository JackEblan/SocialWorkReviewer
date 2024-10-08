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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlinx.coroutines.launch

@Composable
internal fun QuickQuestionsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questions: List<Question>,
    answeredQuestions: Map<Question, List<String>>,
    onUpdateChoice: (question: Question, choice: String) -> Unit,
    onQuitQuestions: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            questions.size
        },
    )

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / questions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var correctScoreCount by rememberSaveable {
        mutableIntStateOf(0)
    }

    var wrongScoreCount by rememberSaveable {
        mutableIntStateOf(0)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            QuickQuestionTopBar(
                wrongScoreCount = wrongScoreCount,
                correctScoreCount = correctScoreCount,
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

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                QuickQuestionPage(
                    page = page,
                    questions = questions,
                    answeredQuestions = answeredQuestions,
                    onUpdateChoice = { question, choice, isCorrect ->
                        if (isCorrect) {
                            correctScoreCount++
                        } else {
                            wrongScoreCount++
                        }

                        onUpdateChoice(question, choice)
                    },
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
            dialogTitle = "Quit Quick Questions",
            dialogText = "Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }
}

@Composable
private fun QuickQuestionTopBar(
    modifier: Modifier = Modifier,
    wrongScoreCount: Int,
    correctScoreCount: Int,
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

        SlideDownToUpText(text = "$wrongScoreCount", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(20.dp))

        Icon(imageVector = Swr.Check, contentDescription = "Correct")

        Spacer(modifier = Modifier.width(5.dp))

        SlideDownToUpText(text = "$correctScoreCount", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun QuickQuestionPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    questions: List<Question>,
    answeredQuestions: Map<Question, List<String>>,
    onUpdateChoice: (question: Question, choice: String, isCorrect: Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        Spacer(modifier = Modifier.height(10.dp))

        QuickQuestionChoicesSelection(
            question = questions[page],
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            selectedChoices = answeredQuestions[questions[page]] ?: emptyList(),
            onUpdateChoice = onUpdateChoice,
        )
    }
}

@Composable
private fun QuickQuestionChoicesSelection(
    modifier: Modifier = Modifier,
    question: Question,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>,
    onUpdateChoice: (question: Question, choice: String, isCorrect: Boolean) -> Unit,
) {
    val greenGradientColors = listOf(
        Color(0xFF43A047),
        Color(0xFF7CB342),
        Color(0xFF039BE5),
    )

    val redGradientColors = listOf(Color.Red, Color.Blue, Color.Red)

    val wrongChoiceAnimation = remember { Animatable(0f) }

    val correctChoiceAnimation = remember { Animatable(1f) }

    val scope = rememberCoroutineScope()

    var lastSelectedChoiceIndex by remember {
        mutableIntStateOf(-1)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
    ) {
        choices.forEachIndexed { index, choice ->
            val selectedChoice = choice in selectedChoices

            val selectedChoiceSizeLimit = selectedChoices.size == correctChoices.size

            val correctChoice = selectedChoiceSizeLimit && choice in correctChoices

            val wrongChoice =
                selectedChoiceSizeLimit && choice !in correctChoices && choice in selectedChoices

            val canSelect = selectedChoice.not() && selectedChoices.size < correctChoices.size

            val choiceBorderGradientColors = if (selectedChoice && correctChoice) {
                greenGradientColors
            } else if (correctChoice) {
                greenGradientColors
            } else if (wrongChoice) {
                redGradientColors
            } else if (selectedChoice) {
                greenGradientColors
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
                onClick = {
                    if (canSelect) {
                        lastSelectedChoiceIndex = index

                        onUpdateChoice(
                            question,
                            choice,
                            choice in correctChoices,
                        )

                        scope.launch {
                            correctChoiceAnimation.animateTo(
                                targetValue = 1f,
                                animationSpec = keyframes {
                                    durationMillis = 300
                                    1f at 0
                                    1.1f at 150
                                },
                            )

                            wrongChoiceAnimation.animateTo(
                                targetValue = 0f,
                                animationSpec = keyframes {
                                    durationMillis = 300
                                    50f at 100
                                    (-50f) at 200
                                },
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        if (wrongChoice) {
                            translationX = wrongChoiceAnimation.value
                        } else if (correctChoice || lastSelectedChoiceIndex == index) {
                            scaleX = correctChoiceAnimation.value
                            scaleY = correctChoiceAnimation.value
                        }
                    },
                border = BorderStroke(
                    width = if (selectedChoice || correctChoice || wrongChoice) 2.dp else 1.dp,
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
