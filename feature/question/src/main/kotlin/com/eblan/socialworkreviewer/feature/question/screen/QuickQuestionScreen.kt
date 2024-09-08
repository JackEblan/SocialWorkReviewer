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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLargeTopAppBar
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuickQuestionsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questions: List<Question>,
    currentQuestionData: QuestionData,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onQuitQuestions: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            questions.size
        },
    )

    val scrollBehavior = enterAlwaysScrollBehavior()

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / questions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold(
        topBar = {
            SwrLargeTopAppBar(
                title = {
                    Text(
                        text = "Quick Questions",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(
                                colors = LocalGradientColors.current.topBarTitleColorsDefault,
                            ),
                        ),
                    )
                },
                modifier = modifier.testTag("quickQuestion:largeTopAppBar"),
                scrollBehavior = scrollBehavior,
            )
        },
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
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    page = page,
                    questions = questions,
                    currentQuestionData = currentQuestionData,
                    onUpdateChoice = onUpdateChoice,
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
private fun QuickQuestionPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    questions: List<Question>,
    currentQuestionData: QuestionData,
    onUpdateChoice: (Choice) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        ChoicesTypeText(
            numberOfChoices = questions[page].correctChoices.size,
        )

        QuickQuestionChoicesSelection(
            currentQuestion = questions[page],
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            currentQuestionData = currentQuestionData,
            onUpdateChoice = onUpdateChoice,
        )
    }
}

@Composable
private fun QuickQuestionChoicesSelection(
    modifier: Modifier = Modifier,
    currentQuestion: Question,
    choices: List<String>,
    correctChoices: List<String>,
    currentQuestionData: QuestionData,
    onUpdateChoice: (Choice) -> Unit,
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

    val (question, selectedChoices) = currentQuestionData

    val isCurrentQuestion = question == currentQuestion

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
    ) {
        choices.forEach { choice ->
            val selectedChoice = isCurrentQuestion && choice in selectedChoices

            val selectedChoiceSizeLimit = selectedChoices.size == correctChoices.size

            val correctChoice =
                isCurrentQuestion && selectedChoiceSizeLimit && choice in correctChoices

            val wrongChoice =
                isCurrentQuestion && selectedChoiceSizeLimit && choice !in correctChoices && choice in selectedChoices

            val choiceBorderGradientColors = if (selectedChoice && correctChoice) {
                greenGradientColors
            } else if (correctChoice) {
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
                onClick = {
                    if (selectedChoice.not() && selectedChoices.size < correctChoices.size) {
                        onUpdateChoice(
                            Choice(
                                question = question,
                                choice = choice,
                            ),
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
                        } else if (correctChoice) {
                            scaleX = correctChoiceAnimation.value
                            scaleY = correctChoiceAnimation.value
                        }
                    },
                border = BorderStroke(
                    width = 2.dp,
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
