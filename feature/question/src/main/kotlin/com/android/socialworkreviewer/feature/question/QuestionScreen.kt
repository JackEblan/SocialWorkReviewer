package com.android.socialworkreviewer.feature.question

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.model.QuestionSetting
import kotlinx.coroutines.launch

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier, viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val selectedChoices =
        viewModel.currentQuestionWithSelectedChoices.collectAsStateWithLifecycle().value

    val countDownTimeUntilFinished =
        viewModel.countDownTimeUntilFinished.collectAsStateWithLifecycle().value

    val countDownTimerFinished =
        viewModel.countDownTimerFinished.collectAsStateWithLifecycle().value

    val questionsWithSelectedChoicesSize =
        viewModel.questionsWithSelectedChoicesSize.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    QuestionScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        questionUiState = questionUiState,
        selectedChoices = selectedChoices,
        countDownTimeUntilFinished = countDownTimeUntilFinished,
        questionsWithSelectedChoicesSize = questionsWithSelectedChoicesSize,
        countDownTimerFinished = countDownTimerFinished,
        onGetCategory = viewModel::getCategory,
        onStartCountDownTimer = viewModel::startCountDownTimer,
        onCancelCountDownTimer = viewModel::cancelCountDownTimer,
        onAddCurrentQuestion = viewModel::addCurrentQuestion,
        onUpdateChoice = viewModel::updateChoice,
        onShowCorrectChoices = viewModel::showCorrectChoices,
        onStartQuestions = viewModel::startQuestions,
        onStartQuickQuestions = viewModel::startQuickQuestions
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questionUiState: QuestionUiState?,
    selectedChoices: List<String>,
    countDownTimeUntilFinished: String,
    questionsWithSelectedChoicesSize: Int,
    countDownTimerFinished: Boolean,
    onGetCategory: () -> Unit,
    onStartCountDownTimer: () -> Unit,
    onCancelCountDownTimer: () -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowCorrectChoices: (Int) -> Unit,
    onStartQuestions: (Int, QuestionSetting) -> Unit,
    onStartQuickQuestions: () -> Unit,
) {
    AnimatedContent(modifier = modifier,
                    targetState = questionUiState,
                    label = "",
                    transitionSpec = {
                        when (targetState) {
                            QuestionUiState.Loading -> (fadeIn(
                                animationSpec = tween(
                                    220, delayMillis = 90
                                )
                            )).togetherWith(fadeOut(animationSpec = tween(90)))

                            is QuestionUiState.ShowCorrectChoices -> (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                slideOutVertically { height -> -height } + fadeOut())

                            else -> (fadeIn(animationSpec = tween(220, delayMillis = 90)) + scaleIn(
                                initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)
                            )).togetherWith(fadeOut(animationSpec = tween(90)))
                        }
                    }) { state ->
        when (state) {
            is QuestionUiState.Questions -> {
                if (state.questions.isNotEmpty()) {
                    Questions(
                        snackbarHostState = snackbarHostState,
                        questionSettingIndex = state.questionSettingIndex,
                        questions = state.questions,
                        selectedChoices = selectedChoices,
                        questionsWithSelectedChoicesSize = questionsWithSelectedChoicesSize,
                        countDownTimerFinished = countDownTimerFinished,
                        countDownTimeUntilFinished = countDownTimeUntilFinished,
                        onStartCountDownTimer = onStartCountDownTimer,
                        onCancelCountDownTimer = onCancelCountDownTimer,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateChoice = onUpdateChoice,
                        onShowCorrectChoices = onShowCorrectChoices,
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
                    questions = state.questions, selectedChoices = selectedChoices,
                    score = state.score,
                    lastCountDownTime = state.lastCountDownTime,
                    onAddCurrentQuestion = onAddCurrentQuestion,
                )
            }

            is QuestionUiState.OnBoarding -> {
                if (state.category != null && state.category.questionSettings.isNotEmpty()) {
                    SuccessOnBoardingScreen(
                        category = state.category,
                        onStartQuestions = onStartQuestions,
                        onStartQuickQuestions = onStartQuickQuestions
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
    countDownTimerFinished: Boolean,
    countDownTimeUntilFinished: String,
    onStartCountDownTimer: () -> Unit,
    onCancelCountDownTimer: () -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onShowCorrectChoices: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    val scope = rememberCoroutineScope()

    val scrollBehavior = enterAlwaysScrollBehavior()

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / questions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress"
    )

    LaunchedEffect(key1 = true) {
        onStartCountDownTimer()
    }

    LaunchedEffect(key1 = countDownTimerFinished) {
        if (countDownTimerFinished) {
            onCancelCountDownTimer()
            onShowCorrectChoices(questionSettingIndex)
        }
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold(topBar = {
        QuestionTopAppBar(
            title = "Questions",
            scrollBehavior = scrollBehavior,
            countDownTimeUntilFinished = countDownTimeUntilFinished
        )
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = scrollBehavior.state.collapsedFraction == 0.0f,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            FloatingActionButton(onClick = {
                if (questionsWithSelectedChoicesSize < questions.size) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please answer all the questions")
                    }
                } else {
                    onShowCorrectChoices(questionSettingIndex)
                }
            }) {
                Icon(
                    imageVector = SocialWorkReviewerIcons.Check, contentDescription = ""
                )
            }
        }
    }) { paddingValues ->
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
                modifier = Modifier.fillMaxSize(), state = pagerState
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
            numberOfChoices = questions[page].correctChoices.size
        )

        QuestionChoicesSelection(isScrollInProgress = isScrollInProgress,
                                 choices = questions[page].choices,
                                 selectedChoices = selectedChoices,
                                 onUpdateChoice = { choice ->
                                     onUpdateChoice(
                                         Choice(
                                             question = questions[page], choice = choice
                                         )
                                     )
                                 })
    }
}

@Composable
private fun QuestionText(modifier: Modifier = Modifier, question: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = question, style = MaterialTheme.typography.headlineSmall
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
internal fun ChoicesType(
    modifier: Modifier = Modifier, numberOfChoices: Int
) {
    val choicesType = if (numberOfChoices > 1) "Multiple Choices" else "Single Choice"

    Spacer(modifier = Modifier.height(10.dp))

    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .animateContentSize()
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = choicesType,
            style = MaterialTheme.typography.bodySmall
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
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = choice in selectedChoices && isScrollInProgress.not(),
                         onCheckedChange = {
                             onUpdateChoice(choice)
                         })

                Box(
                    modifier = Modifier.fillMaxWidth()
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
    countDownTimeUntilFinished: String,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                ),
            )
        },
        modifier = modifier.testTag("question:largeTopAppBar"),
        actions = {
            ElevatedCard(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = SocialWorkReviewerIcons.AccessTime, contentDescription = "")

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = countDownTimeUntilFinished)
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
            imageVector = SocialWorkReviewerIcons.Question,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}