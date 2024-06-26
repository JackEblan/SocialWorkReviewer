package com.android.socialworkreviewer.feature.question

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.model.Question

@Composable
internal fun AnswerScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    selectedChoices: List<String>,
    score: Int,
    onAddCurrentQuestion: (Question) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            AnswerHeader(
                answerIndex = pagerState.currentPage, score = score, answerSize = questions.size
            )

            HorizontalPager(state = pagerState) { page ->
                AnswerPage(
                    page = page,
                    scrollState = scrollState,
                    questions = questions,
                    selectedChoices = selectedChoices,
                )
            }
        }
    }
}

@Composable
private fun AnswerHeader(
    modifier: Modifier = Modifier, answerIndex: Int, score: Int, answerSize: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        AnswerCounter(
            answerIndex = answerIndex, answerSize = answerSize
        )

        ScoreCounter(score = score, answerSize = answerSize)

        AnswerTimeCounter()
    }
}

@Composable
private fun AnswerCounter(
    modifier: Modifier = Modifier, answerIndex: Int, answerSize: Int
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Answer", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${answerIndex + 1}/$answerSize", style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ScoreCounter(
    modifier: Modifier = Modifier, score: Int, answerSize: Int
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Score", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$score/$answerSize", style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun AnswerTimeCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "10", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun AnswerPage(
    page: Int,
    scrollState: ScrollState,
    questions: List<Question>,
    selectedChoices: List<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        AnswerText(question = questions[page].question)

        AnswerChoices(
            choices = questions[page].correctChoices.plus(questions[page].wrongChoices),
            correctChoices = questions[page].correctChoices,
            selectedChoices = selectedChoices,
        )
    }
}

@Composable
private fun AnswerText(modifier: Modifier = Modifier, question: String) {
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
private fun AnswerChoices(
    modifier: Modifier = Modifier,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            val correctAnswer = choice in correctChoices || choice in selectedChoices

            val wrongAnswer = choice !in correctChoices && choice in selectedChoices

            val wrongAnswerColor = CheckboxDefaults.colors().copy(
                checkedBoxColor = Color.Red, checkedBorderColor = Color.Red
            )

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = correctAnswer || wrongAnswer,
                    onCheckedChange = {},
                    colors = if (wrongAnswer) wrongAnswerColor else CheckboxDefaults.colors()
                )

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
