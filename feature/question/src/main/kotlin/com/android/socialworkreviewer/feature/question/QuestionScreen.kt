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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question

@Composable
internal fun QuestionRoute(
    modifier: Modifier = Modifier, viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionUiState = viewModel.questionUiState.collectAsStateWithLifecycle().value

    QuestionScreen(
        modifier = modifier,
        questionUiState = questionUiState,
        onReadyClick = viewModel::getQuestions,
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier,
    questionUiState: QuestionUiState,
    onReadyClick: () -> Unit,
) {

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            when (questionUiState) {
                is QuestionUiState.Success -> {
                    if (questionUiState.questions.isNotEmpty()) {
                        QuestionHeader(
                            questionIndex = 0, questionSize = questionUiState.questions.size
                        )

                        QuestionBody(
                            questions = questionUiState.questions,
                        )
                    }
                }

                QuestionUiState.Loading -> {

                }

                QuestionUiState.Ready -> {
                    ReadyScreen(onReadyClick = onReadyClick)
                }

                QuestionUiState.Finish -> {
                    FinishScreen()
                }
            }
        }
    }
}

@Composable
private fun ReadyScreen(modifier: Modifier = Modifier, onReadyClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Get Ready", style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = onReadyClick) {
            Text(text = "Ready")
        }
    }
}

@Composable
private fun FinishScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your score is", style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun QuestionHeader(modifier: Modifier = Modifier, questionIndex: Int, questionSize: Int) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuestionCounter(
            questionIndex = questionIndex, questionSize = questionSize
        )

        TimeCounter()

        ScoreCounter()
    }
}

@Composable
private fun QuestionBody(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
) {

    val questionIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier.verticalScroll(scrollState), verticalArrangement = Arrangement.Center
    ) {
        QuestionText(question = questions[questionIndex].question)

        if (questions[questionIndex].answersId.size > 1) {
            MultipleChoices(
                choices = questions[questionIndex].choices,
            )
        } else {
            SingleChoice(
                choices = questions[questionIndex].choices,
            )
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
private fun ScoreCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Score", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "15", style = MaterialTheme.typography.titleLarge)
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
private fun SingleChoice(
    modifier: Modifier = Modifier,
    choices: List<Choice>,
) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        choices.forEach { choice ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = false, onCheckedChange = {})

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
private fun MultipleChoices(
    modifier: Modifier = Modifier,
    choices: List<Choice>,
) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        choices.forEach { choice ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = false, onCheckedChange = {})

                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(text = choice.content)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}