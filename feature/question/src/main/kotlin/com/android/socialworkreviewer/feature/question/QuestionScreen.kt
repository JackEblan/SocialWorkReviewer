package com.android.socialworkreviewer.feature.question

import androidx.annotation.VisibleForTesting
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
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
        onReadyClick = viewModel::getQuestions
    )
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(
    modifier: Modifier = Modifier, questionUiState: QuestionUiState, onReadyClick: () -> Unit
) {

    val questionIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            when (questionUiState) {
                is QuestionUiState.Success -> {
                    if (questionUiState.questions.isNotEmpty()) {
                        QuestionHeader()

                        QuestionBody(
                            modifier = Modifier.weight(1f),
                            question = questionUiState.questions[questionIndex],
                        )
                    }
                }

                QuestionUiState.Loading -> {

                }

                QuestionUiState.OnBoarding -> {
                    OnBoarding(onReadyClick = onReadyClick)
                }
            }
        }
    }
}

@Composable
private fun OnBoarding(modifier: Modifier = Modifier, onReadyClick: () -> Unit) {
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
private fun QuestionHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuestionCounter()

        TimeCounter()

        ScoreCounter()
    }
}

@Composable
private fun QuestionBody(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    question: Question
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        QuestionText(question = question.question)

        ChoicesButtons(choices = question.choices, answerId = question.answerId)
    }
}

@Composable
private fun QuestionCounter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Question", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "2/10", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun TimeCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = ":08", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun ScoreCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Score", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "0", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun QuestionText(modifier: Modifier = Modifier, question: String) {
    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = question, style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun ChoicesButtons(modifier: Modifier = Modifier, choices: List<Choice>, answerId: String) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        choices.forEach { choice ->
            OutlinedButton(onClick = {

            }, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = SocialWorkReviewerIcons.Check, contentDescription = "")

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = choice.content)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}