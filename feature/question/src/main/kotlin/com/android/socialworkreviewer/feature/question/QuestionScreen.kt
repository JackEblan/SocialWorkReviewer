package com.android.socialworkreviewer.feature.question

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.launch

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier, viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val selectedChoices = viewModel.selectedChoices.collectAsStateWithLifecycle().value

    val scoreCount = viewModel.scoreCount.collectAsStateWithLifecycle().value

    val answeredQuestionsCount =
        viewModel.answeredQuestionsCount.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    QuestionScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        questionUiState = questionUiState,
        selectedChoices = selectedChoices,
        scoreCount = scoreCount,
        answeredQuestionsCount = answeredQuestionsCount, onGetCategory = viewModel::getCategory,
        onAddQuestions = viewModel::addQuestions,
        onAddCurrentQuestion = viewModel::addCurrentQuestion,
        onUpdateAnswer = viewModel::updateChoice,
        onShowAnswers = viewModel::showCorrectChoices,
        onGetQuestions = viewModel::getQuestions
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState, questionUiState: QuestionUiState?,
    selectedChoices: List<String>,
    scoreCount: Int,
    answeredQuestionsCount: Int, onGetCategory: () -> Unit,
    onAddQuestions: (List<Question>) -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateAnswer: (Choice) -> Unit,
    onShowAnswers: () -> Unit,
    onGetQuestions: (Int, Int) -> Unit
) {
    Crossfade(modifier = modifier, targetState = questionUiState, label = "") { state ->
        when (state) {
            is QuestionUiState.Questions -> {
                if (state.questions.isNotEmpty()) {
                    Questions(
                        snackbarHostState = snackbarHostState,
                        questions = state.questions, selectedChoices = selectedChoices,
                        answeredQuestionsCount = answeredQuestionsCount,
                        onAddQuestions = onAddQuestions,
                        onAddCurrentQuestion = onAddCurrentQuestion,
                        onUpdateAnswer = onUpdateAnswer,
                        onShowAnswers = onShowAnswers,
                    )
                }
            }

            QuestionUiState.Loading -> {
                LoadingScreen()
            }

            is QuestionUiState.ShowCorrectChoices -> {
                CorrectChoicesScreen(
                    questions = state.questions, selectedChoices = selectedChoices,
                    score = scoreCount,
                    onAddCurrentQuestion = onAddCurrentQuestion,
                )
            }

            is QuestionUiState.OnBoarding -> {
                if (state.category != null) {
                    SuccessOnBoardingScreen(
                        category = state.category, onGetQuestions = onGetQuestions
                    )
                }
            }

            null -> {
                LoadingOnBoardingScreen(onGetCategory = onGetCategory)
            }
        }
    }
}

@Composable
private fun QuestionHeader(
    modifier: Modifier = Modifier, questionIndex: Int, questionSize: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuestionCounter(
            questionIndex = questionIndex, questionSize = questionSize
        )

        QuestionTimeCounter()
    }
}

@Composable
private fun Questions(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    selectedChoices: List<String>,
    answeredQuestionsCount: Int,
    onAddQuestions: (List<Question>) -> Unit,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateAnswer: (Choice) -> Unit,
    onShowAnswers: () -> Unit,
) {

    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        onAddQuestions(questions)
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            if (answeredQuestionsCount < questions.size) {
                scope.launch {
                    snackbarHostState.showSnackbar("Please answer all the questions")
                }
            } else {
                onShowAnswers()
            }
        }) {
            Icon(
                imageVector = SocialWorkReviewerIcons.Check, contentDescription = ""
            )
        }
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            QuestionHeader(
                questionIndex = pagerState.currentPage, questionSize = questions.size
            )

            HorizontalPager(state = pagerState) { page ->
                QuestionPage(
                    page = page,
                    isScrollInProgress = pagerState.isScrollInProgress,
                    scrollState = scrollState,
                    questions = questions,
                    selectedChoices = selectedChoices,
                    onUpdateAnswer = onUpdateAnswer,
                )
            }
        }
    }
}

@Composable
private fun QuestionPage(
    page: Int,
    isScrollInProgress: Boolean,
    scrollState: ScrollState,
    questions: List<Question>,
    selectedChoices: List<String>,
    onUpdateAnswer: (Choice) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuestionText(question = questions[page].question)

        QuestionChoices(isScrollInProgress = isScrollInProgress,
                        choices = questions[page].choices,
                        selectedChoices = selectedChoices,
                        onUpdateAnswer = { choice ->
                            onUpdateAnswer(
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
            .padding(20.dp)
    ) {
        Text(
            text = question, style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun QuestionCounter(
    modifier: Modifier = Modifier, questionIndex: Int, questionSize: Int
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Question", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${questionIndex + 1}/$questionSize", style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun QuestionTimeCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "10", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun QuestionChoices(
    modifier: Modifier = Modifier,
    isScrollInProgress: Boolean,
    choices: List<String>,
    selectedChoices: List<String>,
    onUpdateAnswer: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onUpdateAnswer(choice)
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = choice in selectedChoices && isScrollInProgress.not(),
                         onCheckedChange = {
                             onUpdateAnswer(choice)
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