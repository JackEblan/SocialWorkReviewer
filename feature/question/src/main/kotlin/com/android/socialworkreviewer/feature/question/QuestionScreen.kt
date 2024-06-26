package com.android.socialworkreviewer.feature.question

import androidx.annotation.VisibleForTesting
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerLoadingWheel
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Choice
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
        onAddAnswer = viewModel::addAnswer,
        onRemoveAnswer = viewModel::removeAnswer
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    questionUiState: QuestionUiState,
    answers: List<Answer>,
    onGetQuestions: () -> Unit,
    onAddAnswer: (Answer) -> Unit,
    onRemoveAnswer: (Answer) -> Unit
) {
    LaunchedEffect(key1 = true) {
        onGetQuestions()
    }

    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(imageVector = SocialWorkReviewerIcons.Check, contentDescription = "")
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            when (questionUiState) {
                is QuestionUiState.Success -> {
                    if (questionUiState.questions.isNotEmpty()) {
                        SuccessState(
                            questions = questionUiState.questions, answers = answers,
                            onAddAnswer = onAddAnswer,
                            onRemoveAnswer = onRemoveAnswer,
                        )
                    }
                }

                QuestionUiState.Loading -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
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

        TimeCounter()
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    answers: List<Answer>,
    onAddAnswer: (Answer) -> Unit,
    onRemoveAnswer: (Answer) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    Column(modifier = modifier) {
        QuestionHeader(
            questionIndex = pagerState.currentPage, questionSize = questions.size
        )

        HorizontalPager(state = pagerState) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                QuestionText(question = questions[page].question)

                Choices(choices = questions[page].correctAnswers.plus(questions[page].wrongAnswers),
                        selectedChoices = answers.map { it.choice },
                        onAddChoice = { choice ->
                            onAddAnswer(Answer(question = questions[page], choice = choice))
                        },
                        onRemoveChoice = { choice ->
                            onRemoveAnswer(Answer(question = questions[page], choice = choice))
                        })
            }
        }
    }
}

@Composable
private fun QuestionCounter(modifier: Modifier = Modifier, questionIndex: Int, questionSize: Int) {
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
private fun TimeCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "10", style = MaterialTheme.typography.titleLarge)
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
private fun Choices(
    modifier: Modifier = Modifier,
    choices: List<Choice>,
    selectedChoices: List<Choice>,
    onAddChoice: (Choice) -> Unit,
    onRemoveChoice: (Choice) -> Unit,
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
                    if (choice !in selectedChoices) {
                        onAddChoice(choice)
                    } else {
                        onRemoveChoice(choice)
                    }
                })

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = choice.content)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SocialWorkReviewerLoadingWheel(
        modifier = modifier,
        contentDescription = "SocialWorkReviewerLoadingWheel",
    )
}