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
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.CountDownTime
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionSetting
import com.eblan.socialworkreviewer.feature.question.QuestionUiState
import com.eblan.socialworkreviewer.feature.question.QuestionViewModel
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlinx.coroutines.launch

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val countDownTime = viewModel.countDownTime.collectAsStateWithLifecycle().value

    val answeredQuestions = viewModel.answeredQuestionsFlow.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    QuestionScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        questionUiState = questionUiState,
        countDownTime = countDownTime,
        answeredQuestions = answeredQuestions,
        onUpdateChoice = viewModel::updateChoice,
        onShowCorrectChoices = viewModel::showCorrectChoices,
        onStartQuestions = viewModel::startQuestions,
        onStartQuickQuestions = viewModel::startQuickQuestions,
        onQuitQuestions = {
            viewModel.clearCache()
            onNavigateUp()
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questionUiState: QuestionUiState?,
    answeredQuestions: Map<Question, List<String>>,
    countDownTime: CountDownTime?,
    onUpdateChoice: (question: Question, choice: String) -> Unit,
    onShowCorrectChoices: (questionSettingIndex: Int, questions: List<Question>, score: Int) -> Unit,
    onStartQuestions: (Int, QuestionSetting) -> Unit,
    onStartQuickQuestions: () -> Unit,
    onQuitQuestions: () -> Unit,
) {
    AnimatedContent(
        modifier = modifier.semantics { testTagsAsResourceId = true },
        targetState = questionUiState,
        label = "",
        transitionSpec = {
            when (targetState) {
                is QuestionUiState.CorrectChoices, is QuestionUiState.Questions, is QuestionUiState.QuickQuestions -> {
                    (slideInVertically() + fadeIn()).togetherWith(
                        slideOutVertically() + fadeOut(),
                    )
                }

                else -> {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90))).togetherWith(
                        fadeOut(),
                    )
                }
            }
        },
    ) { state ->
        when (state) {
            is QuestionUiState.Questions -> {
                if (state.questions.isNotEmpty()) {
                    Questions(
                        snackbarHostState = snackbarHostState,
                        answeredQuestions = answeredQuestions,
                        questionSettingIndex = state.questionSettingIndex,
                        questions = state.questions,
                        countDownTime = countDownTime,
                        onUpdateChoice = onUpdateChoice,
                        onShowCorrectChoices = onShowCorrectChoices,
                        onQuitQuestions = onQuitQuestions,
                    )
                } else {
                    EmptyState(text = "No Questions found!")
                }
            }

            QuestionUiState.Loading, null -> {
                LoadingScreen()
            }

            is QuestionUiState.CorrectChoices -> {
                CorrectChoicesScreen(
                    score = state.score,
                    questions = state.questions,
                    answeredQuestions = answeredQuestions,
                    lastCountDownTime = state.lastCountDownTime,
                    onQuitQuestions = onQuitQuestions,
                )
            }

            is QuestionUiState.OnBoarding -> {
                if (state.category != null && state.category.questionSettings.isNotEmpty()) {
                    SuccessOnBoardingScreen(
                        category = state.category,
                        statistics = state.statistics,
                        onStartQuestions = onStartQuestions,
                        onStartQuickQuestions = onStartQuickQuestions,
                    )
                } else {
                    EmptyState(text = "No Question Settings found!")
                }
            }

            is QuestionUiState.QuickQuestions -> {
                if (state.questions.isNotEmpty()) {
                    QuickQuestionsScreen(
                        snackbarHostState = snackbarHostState,
                        questions = state.questions,
                        answeredQuestions = answeredQuestions,
                        onUpdateChoice = onUpdateChoice,
                        onQuitQuestions = onQuitQuestions,
                    )
                }
            }
        }
    }
}

@Composable
private fun Questions(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    answeredQuestions: Map<Question, List<String>>,
    questionSettingIndex: Int,
    questions: List<Question>,
    countDownTime: CountDownTime?,
    onUpdateChoice: (question: Question, choice: String) -> Unit,
    onShowCorrectChoices: (questionSettingIndex: Int, questions: List<Question>, score: Int) -> Unit,
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

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    LaunchedEffect(key1 = countDownTime) {
        if (countDownTime != null && countDownTime.isFinished) {
            onShowCorrectChoices(questionSettingIndex, questions, correctScoreCount)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = answeredQuestions.size == questions.size,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowCorrectChoices(questionSettingIndex, questions, correctScoreCount)
                    },
                ) {
                    Icon(
                        imageVector = Swr.Check,
                        contentDescription = "Check Button",
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            QuestionTopBar(
                answeredQuestionSize = answeredQuestions.size,
                countDownTime = countDownTime,
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
                QuestionPage(
                    page = page,
                    questions = questions,
                    selectedChoices = answeredQuestions[questions[page]] ?: emptyList(),
                    onUpdateChoice = { question, choice, isCorrect ->
                        if (isCorrect) {
                            correctScoreCount++
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
            dialogTitle = "Quit Questions",
            dialogText = "You have answered ${answeredQuestions.size} out of ${questions.size} questions. Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }
}

@Composable
private fun QuestionTopBar(
    modifier: Modifier = Modifier,
    answeredQuestionSize: Int,
    countDownTime: CountDownTime?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Swr.Check, contentDescription = "Questions With Selected Choices")

        Spacer(modifier = Modifier.width(5.dp))

        SlideDownToUpText(text = "$answeredQuestionSize", fontWeight = FontWeight.Bold)

        if (countDownTime != null && countDownTime.isFinished.not()) {
            Spacer(modifier = Modifier.width(20.dp))

            Icon(imageVector = Swr.AccessTime, contentDescription = "Minutes")

            Spacer(modifier = Modifier.width(5.dp))

            SlideDownToUpText(text = countDownTime.minutes, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun QuestionPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    questions: List<Question>,
    selectedChoices: List<String>,
    onUpdateChoice: (question: Question, choice: String, isCorrect: Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        Spacer(modifier = Modifier.height(10.dp))

        QuestionChoicesSelection(
            currentQuestion = questions[page],
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            selectedChoices = selectedChoices,
            onUpdateChoice = onUpdateChoice,
        )
    }
}

@Composable
internal fun QuestionText(modifier: Modifier = Modifier, question: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = AnnotatedString.fromHtml(question),
            style = MaterialTheme.typography.headlineSmall,
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun QuestionChoicesSelection(
    modifier: Modifier = Modifier,
    currentQuestion: Question,
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

    val choiceAnimation = remember { Animatable(1f) }

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

            val choiceBorderGradientColors = if (selectedChoice) {
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
                    lastSelectedChoiceIndex = index

                    onUpdateChoice(
                        currentQuestion,
                        choice,
                        choice in correctChoices,
                    )

                    scope.launch {
                        choiceAnimation.animateTo(
                            targetValue = 1f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                1f at 0
                                1.1f at 150
                            },
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        if (lastSelectedChoiceIndex == index) {
                            scaleX = choiceAnimation.value
                            scaleY = choiceAnimation.value
                        }
                    },
                border = BorderStroke(
                    width = if (selectedChoice) 2.dp else 1.dp,
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

@Composable
internal fun SlideDownToUpText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight? = null,
) {
    Row {
        text.indices.forEach { i ->
            AnimatedContent(
                targetState = text[i],
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                },
                label = "",
            ) { char ->
                Text(
                    text = "$char",
                    modifier = modifier,
                    fontWeight = fontWeight,
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("question:emptyListPlaceHolderScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Swr.Question,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
