package com.android.socialworkreviewer.feature.question

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons

@Composable
internal fun QuestionRoute(modifier: Modifier = Modifier) {
    QuestionScreen(modifier = modifier)
}

@VisibleForTesting
@Composable
internal fun QuestionScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(imageVector = SocialWorkReviewerIcons.Paused, contentDescription = "")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            QuestionHeader()

            QuestionBody(modifier = Modifier.weight(1f))
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
    modifier: Modifier = Modifier, scrollState: ScrollState = rememberScrollState()
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Question()

        Choices()
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
private fun Question(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "How old are you?", style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun Choices(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "One")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Two")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Three")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Four")
        }
    }
}