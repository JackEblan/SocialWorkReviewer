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
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLargeTopAppBar
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.CountDownTime
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import com.eblan.socialworkreviewer.core.model.QuestionSetting
import com.eblan.socialworkreviewer.feature.question.QuestionUiState
import com.eblan.socialworkreviewer.feature.question.QuestionViewModel
import com.eblan.socialworkreviewer.feature.question.dialog.question.QuestionsDialog
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlinx.coroutines.launch

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val currentQuestionData = viewModel.currentQuestionData.collectAsStateWithLifecycle().value

    val countDownTime = viewModel.countDownTime.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    QuestionScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        questionUiState = questionUiState,
        currentQuestionData = currentQuestionData,
        countDownTime = countDownTime,
        onGetCategory = viewModel::getCategory,
        onAddCurrentQuestion = viewModel::addCurrentQuestion,
        onUpdateChoice = viewModel::updateChoice,
        onShowCorrectChoices = viewModel::showCorrectChoices,
        onShowScore = viewModel::showScore,
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
    currentQuestionData: QuestionData,
    countDownTime: CountDownTime?,
    onGetCategory: () -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowCorrectChoices: (questions: List<Question>) -> Unit,
    onShowScore: (questionSettingIndex: Int, questions: List<Question>) -> Unit,
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
                is QuestionUiState.Score, is QuestionUiState.CorrectChoices, is QuestionUiState.Questions, is QuestionUiState.QuickQuestions -> {
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
                        currentQuestionData = currentQuestionData,
                        questionSettingIndex = state.questionSettingIndex,
                        questions = state.questions,
                        countDownTime = countDownTime,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateChoice = onUpdateChoice,
                        onShowScore = onShowScore,
                        onQuitQuestions = onQuitQuestions,
                    )
                } else {
                    EmptyState(text = "No Question found!")
                }
            }

            QuestionUiState.Loading -> {
                LoadingScreen()
            }

            is QuestionUiState.CorrectChoices -> {
                CorrectChoicesScreen(
                    questions = state.questions,
                    currentQuestionData = currentQuestionData,
                    onAddCurrentQuestion = onAddCurrentQuestion,
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

            null -> {
                LoadingOnBoardingScreen(onGetCategory = onGetCategory)
            }

            is QuestionUiState.QuickQuestions -> {
                if (state.questions.isNotEmpty()) {
                    QuickQuestionsScreen(
                        snackbarHostState = snackbarHostState,
                        questions = state.questions,
                        currentQuestionData = currentQuestionData,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateChoice = onUpdateChoice,
                        onQuitQuestions = onQuitQuestions,
                    )
                }
            }

            is QuestionUiState.Score -> {
                ScoreScreen(
                    questions = state.questions,
                    score = state.score,
                    minutes = state.lastCountDownTime,
                    onShowCorrectChoices = onShowCorrectChoices,
                    onQuitQuestions = onQuitQuestions,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Questions(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    currentQuestionData: QuestionData,
    questionSettingIndex: Int,
    questions: List<Question>,
    countDownTime: CountDownTime?,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowScore: (questionSettingIndex: Int, questions: List<Question>) -> Unit,
    onQuitQuestions: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            questions.size
        },
    )

    val scope = rememberCoroutineScope()

    val scrollBehavior = enterAlwaysScrollBehavior()

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / questions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showQuestionsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    LaunchedEffect(key1 = countDownTime) {
        if (countDownTime != null && countDownTime.isFinished) {
            onShowScore(questionSettingIndex, questions)
        }
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
                        text = "Questions",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(
                                colors = LocalGradientColors.current.topBarTitleColorsDefault,
                            ),
                        ),
                    )
                },
                modifier = modifier.testTag("question:largeTopAppBar"),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = scrollBehavior.state.collapsedFraction == 0.0f,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    onClick = {
                        if (currentQuestionData.questionsWithSelectedChoices.size < questions.size) {
                            showQuestionsDialog = true
                        } else {
                            onShowScore(questionSettingIndex, questions)
                        }
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
            dialogTitle = "Quit Questions",
            dialogText = "You have answered ${currentQuestionData.questionsWithSelectedChoices.size} out of ${questions.size} questions. Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }

    if (showQuestionsDialog) {
        QuestionsDialog(
            modifier = modifier,
            minutes = countDownTime?.minutes,
            questionsSize = questions.size,
            answeredQuestions = currentQuestionData.answeredQuestions,
            onQuestionClick = { index ->
                scope.launch {
                    showQuestionsDialog = false
                    pagerState.animateScrollToPage(index)
                }
            },
            onOkayClick = {
                showQuestionsDialog = false
            },
            contentDescription = "",
        )
    }
}

@Composable
private fun QuestionPage(
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

        QuestionChoicesSelection(
            currentQuestion = questions[page],
            choices = questions[page].choices,
            currentQuestionData = currentQuestionData,
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
internal fun ChoicesTypeText(
    modifier: Modifier = Modifier,
    numberOfChoices: Int,
) {
    val choicesType = if (numberOfChoices > 1) "Multiple Choices" else "Single Choice"

    Spacer(modifier = Modifier.height(10.dp))

    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .animateContentSize(),
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = choicesType,
            style = MaterialTheme.typography.bodySmall,
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun QuestionChoicesSelection(
    modifier: Modifier = Modifier,
    currentQuestion: Question,
    choices: List<String>,
    currentQuestionData: QuestionData,
    onUpdateChoice: (Choice) -> Unit,
) {
    val greenGradientColors = listOf(
        Color(0xFF43A047),
        Color(0xFF7CB342),
        Color(0xFF039BE5),
    )

    val choiceAnimation = remember { Animatable(1f) }

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

            val selectedChoiceBrush = if (selectedChoice) {
                Brush.linearGradient(
                    colors = greenGradientColors,
                )
            } else {
                CardDefaults.outlinedCardBorder().brush
            }

            OutlinedCard(
                onClick = {
                    onUpdateChoice(
                        Choice(
                            question = currentQuestion,
                            choice = choice,
                        ),
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
                        if (selectedChoice) {
                            scaleX = choiceAnimation.value
                            scaleY = choiceAnimation.value
                        }
                    },
                border = BorderStroke(width = 2.dp, brush = selectedChoiceBrush),
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
