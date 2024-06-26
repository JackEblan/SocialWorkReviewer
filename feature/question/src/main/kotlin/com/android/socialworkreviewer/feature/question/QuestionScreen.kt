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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier, viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    val answers = viewModel.answers.collectAsStateWithLifecycle().value

    QuestionScreen(
        modifier = modifier,
        questionUiState = questionUiState,
        answers = answers,
        onGetQuestions = viewModel::getQuestions,
        onAddQuestions = viewModel::addQuestions,
        onUpdateAnswer = viewModel::updateAnswer,
        onShowAnswers = viewModel::showAnswers
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    questionUiState: QuestionUiState,
    answers: List<Answer>,
    onGetQuestions: () -> Unit,
    onAddQuestions: (List<Question>) -> Unit,
    onUpdateAnswer: (Answer) -> Unit,
    onShowAnswers: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        onGetQuestions()
    }

    Crossfade(modifier = modifier, targetState = questionUiState, label = "") { state ->
        when (state) {
            is QuestionUiState.Success -> {
                if (state.questions.isNotEmpty()) {
                    SuccessState(
                        questions = state.questions, answers = answers,
                        onAddQuestions = onAddQuestions,
                        onUpdateAnswer = onUpdateAnswer,
                        onShowAnswers = onShowAnswers,
                    )
                }
            }

            QuestionUiState.Loading -> {
                Scaffold { paddingValues ->
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .consumeWindowInsets(paddingValues)
                            .padding(paddingValues),
                    ) {
                        LoadingScreen(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            is QuestionUiState.ShowAnswer -> {
                AnswerScreen(
                    questions = state.questions, answers = answers
                )
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
private fun SuccessState(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    answers: List<Answer>,
    onAddQuestions: (List<Question>) -> Unit,
    onUpdateAnswer: (Answer) -> Unit,
    onShowAnswers: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        onAddQuestions(questions)
    }

    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onShowAnswers) {
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

            QuestionPager(
                pagerState = pagerState,
                scrollState = scrollState,
                questions = questions,
                answers = answers,
                onUpdateAnswer = onUpdateAnswer,
            )
        }
    }
}

@Composable
private fun QuestionPager(
    pagerState: PagerState,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    answers: List<Answer>,
    onUpdateAnswer: (Answer) -> Unit,
) {
    HorizontalPager(state = pagerState) { page ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            QuestionText(question = questions[page].question)

            QuestionChoices(choices = questions[page].correctChoices.plus(questions[page].wrongChoices),
                            selectedChoices = answers.filter { it.question == questions[page] }
                                .map { it.choice },
                            onUpdateAnswer = { choice ->
                                onUpdateAnswer(
                                    Answer(
                                        question = questions[page], choice = choice
                                    )
                                )
                            })
        }
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
            text = question, style = MaterialTheme.typography.headlineMedium
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
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = choice in selectedChoices, onCheckedChange = {
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