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
package com.android.socialworkreviewer.feature.question

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.designsystem.icon.Swr
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.CountDownTime
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.model.QuestionSetting
import com.android.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog
import kotlinx.coroutines.launch

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel = hiltViewModel(),
    onQuitQuestions: () -> Unit,
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val selectedChoices =
        viewModel.currentQuestionWithSelectedChoices.collectAsStateWithLifecycle().value

    val countDownTime = viewModel.countDownTime.collectAsStateWithLifecycle().value

    val questionsWithSelectedChoicesSize =
        viewModel.questionsWithSelectedChoicesSize.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    QuestionScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        questionUiState = questionUiState,
        selectedChoices = selectedChoices,
        questionsWithSelectedChoicesSize = questionsWithSelectedChoicesSize,
        countDownTime = countDownTime,
        onGetCategory = viewModel::getCategory,
        onAddCurrentQuestion = viewModel::addCurrentQuestion,
        onUpdateChoice = viewModel::updateChoice,
        onShowCorrectChoices = viewModel::showCorrectChoices,
        onStartQuestions = viewModel::startQuestions,
        onStartQuickQuestions = viewModel::startQuickQuestions,
        onQuitQuestions = {
            viewModel.quitQuestions()
            onQuitQuestions()
        },
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questionUiState: QuestionUiState?,
    selectedChoices: List<String>,
    questionsWithSelectedChoicesSize: Int,
    countDownTime: CountDownTime?,
    onGetCategory: () -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowCorrectChoices: (questionSettingIndex: Int, questions: List<Question>) -> Unit,
    onStartQuestions: (Int, QuestionSetting) -> Unit,
    onStartQuickQuestions: () -> Unit,
    onQuitQuestions: () -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = questionUiState,
        label = "",
        transitionSpec = {
            when (targetState) {
                is QuestionUiState.ShowCorrectChoices, is QuestionUiState.Questions, is QuestionUiState.QuickQuestions -> {
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
                        questionSettingIndex = state.questionSettingIndex,
                        questions = state.questions,
                        selectedChoices = selectedChoices,
                        questionsWithSelectedChoicesSize = questionsWithSelectedChoicesSize,
                        countDownTime = countDownTime,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateChoice = onUpdateChoice,
                        onShowCorrectChoices = onShowCorrectChoices,
                        onQuitQuestions = onQuitQuestions,
                    )
                } else {
                    EmptyState(text = "No Question found!")
                }
            }

            QuestionUiState.Loading -> {
                LoadingScreen()
            }

            is QuestionUiState.ShowCorrectChoices -> {
                CorrectChoicesScreen(
                    questions = state.questions,
                    selectedChoices = selectedChoices,
                    score = state.score,
                    minutes = state.lastCountDownTime,
                    onAddCurrentQuestion = onAddCurrentQuestion,
                )
            }

            is QuestionUiState.OnBoarding -> {
                if (state.category != null && state.category.questionSettings.isNotEmpty()) {
                    SuccessOnBoardingScreen(
                        category = state.category,
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
                        selectedChoices = selectedChoices,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateChoice = onUpdateChoice,
                        onQuitQuestions = onQuitQuestions,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Questions(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questionSettingIndex: Int,
    questions: List<Question>,
    selectedChoices: List<String>,
    questionsWithSelectedChoicesSize: Int,
    countDownTime: CountDownTime?,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowCorrectChoices: (questionSettingIndex: Int, questions: List<Question>) -> Unit,
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

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    LaunchedEffect(key1 = countDownTime) {
        if (countDownTime != null && countDownTime.isFinished) {
            onShowCorrectChoices(questionSettingIndex, questions)
        }
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold(
        topBar = {
            QuestionTopAppBar(
                title = "Questions",
                scrollBehavior = scrollBehavior,
                minutes = countDownTime?.minutes,
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
                        if (questionsWithSelectedChoicesSize < questions.size) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please answer all the questions")
                            }
                        } else {
                            onShowCorrectChoices(questionSettingIndex, questions)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Swr.Check,
                        contentDescription = "",
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
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(10.dp),
                progress = {
                    animatedProgress
                },
                strokeCap = StrokeCap.Round,
            )

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                QuestionPage(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    page = page,
                    isScrollInProgress = pagerState.isScrollInProgress,
                    questions = questions,
                    selectedChoices = selectedChoices,
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
            dialogText = "You have answered $questionsWithSelectedChoicesSize out of ${questions.size} questions. Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }
}

@Composable
private fun QuestionPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    isScrollInProgress: Boolean,
    questions: List<Question>,
    selectedChoices: List<String>,
    onUpdateChoice: (Choice) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        ChoicesType(
            numberOfChoices = questions[page].correctChoices.size,
        )

        QuestionChoicesSelection(
            isScrollInProgress = isScrollInProgress,
            choices = questions[page].choices,
            selectedChoices = selectedChoices,
            onUpdateChoice = { choice ->
                onUpdateChoice(
                    Choice(
                        question = questions[page],
                        choice = choice,
                    ),
                )
            },
        )
    }
}

@Composable
private fun QuestionText(modifier: Modifier = Modifier, question: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineSmall,
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
internal fun ChoicesType(
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
    isScrollInProgress: Boolean,
    choices: List<String>,
    selectedChoices: List<String>,
    onUpdateChoice: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onUpdateChoice(choice)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = choice in selectedChoices && isScrollInProgress.not(),
                    onCheckedChange = {
                        onUpdateChoice(choice)
                    },
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = choice)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    minutes: String?,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                    ),
                ),
            )
        },
        modifier = modifier.testTag("question:largeTopAppBar"),
        actions = {
            if (minutes.isNullOrBlank().not()) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .animateContentSize(),
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Swr.AccessTime,
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = minutes!!)
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
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
