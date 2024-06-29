package com.android.socialworkreviewer.feature.question

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.model.Question

@Composable
internal fun CorrectChoicesScreen(
    modifier: Modifier = Modifier,
    questions: List<Question>,
    selectedChoices: List<String>,
    score: Int,
    lastCountDownTime: String,
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
            CorrectChoicesHeader(
                answerIndex = pagerState.currentPage,
                score = score,
                answerSize = questions.size,
                lastCountDownTime = lastCountDownTime
            )

            HorizontalPager(state = pagerState) { page ->
                CorrectChoicesPage(
                    page = page,
                    isScrollInProgress = pagerState.isScrollInProgress,
                    questions = questions,
                    selectedChoices = selectedChoices,
                )
            }
        }
    }
}

@Composable
private fun CorrectChoicesHeader(
    modifier: Modifier = Modifier,
    answerIndex: Int,
    score: Int,
    answerSize: Int,
    lastCountDownTime: String
) {
    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        CorrectChoicesQuestionCounter(
            modifier = Modifier.weight(1f), answerIndex = answerIndex, answerSize = answerSize
        )

        CorrectChoicesScoreCounter(
            modifier = Modifier.weight(2f), score = score, answerSize = answerSize
        )

        CorrectChoicesTimeCounter(
            modifier = Modifier.weight(1f), lastCountDownTime = lastCountDownTime
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun CorrectChoicesQuestionCounter(
    modifier: Modifier = Modifier, answerIndex: Int, answerSize: Int
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Answer", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "${answerIndex + 1}/$answerSize", style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun CorrectChoicesScoreCounter(
    modifier: Modifier = Modifier, score: Int, answerSize: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF00BCD4),
        targetValue = Color(0xFF9C27B0),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000), repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Score", style = MaterialTheme.typography.headlineSmall, color = animatedColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$score/$answerSize",
            style = MaterialTheme.typography.headlineSmall,
            color = animatedColor
        )
    }
}

@Composable
private fun CorrectChoicesTimeCounter(modifier: Modifier = Modifier, lastCountDownTime: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = lastCountDownTime, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CorrectChoicesPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    isScrollInProgress: Boolean,
    questions: List<Question>,
    selectedChoices: List<String>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        CorrectChoicesQuestionText(question = questions[page].question)

        CorrectChoicesSelection(
            isScrollInProgress = isScrollInProgress,
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            selectedChoices = selectedChoices,
        )
    }
}

@Composable
private fun CorrectChoicesQuestionText(modifier: Modifier = Modifier, question: String) {
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
private fun CorrectChoicesSelection(
    modifier: Modifier = Modifier,
    isScrollInProgress: Boolean,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>,
) {
    val wrongChoiceColor = CheckboxDefaults.colors().copy(
        checkedBoxColor = Color.Red, checkedBorderColor = Color.Red
    )

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            val correctChoice = isScrollInProgress.not() && choice in correctChoices

            val wrongChoice =
                isScrollInProgress.not() && choice !in correctChoices && choice in selectedChoices

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = correctChoice || wrongChoice,
                    onCheckedChange = {},
                    colors = if (wrongChoice) wrongChoiceColor else CheckboxDefaults.colors()
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
