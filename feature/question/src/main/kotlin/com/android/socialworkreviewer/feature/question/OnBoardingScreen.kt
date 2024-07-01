package com.android.socialworkreviewer.feature.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.model.QuestionSetting

@Composable
internal fun LoadingOnBoardingScreen(
    modifier: Modifier = Modifier,
    onGetCategory: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        onGetCategory()
    }

    LoadingScreen(modifier = modifier)
}

@Composable
internal fun SuccessOnBoardingScreen(
    modifier: Modifier = Modifier,
    category: Category,
    onGetQuestions: (Int, QuestionSetting) -> Unit,
    onGetQuickQuestions: () -> Unit,
) {

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onGetQuickQuestions) {
            Icon(imageVector = SocialWorkReviewerIcons.Bolt, contentDescription = "")
        }
    }, topBar = {
        OnBoardingTopAppBar(title = "Questions")
    }) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = paddingValues,
        ) {
            itemsIndexed(category.questionSettings) { index, questionSetting ->
                ElevatedCard(modifier = Modifier.padding(10.dp), onClick = {
                    onGetQuestions(
                        index, questionSetting
                    )

                }) {
                    QuestionSettingItem(questionSetting = questionSetting)
                }
            }
        }
    }
}

@Composable
private fun QuestionSettingItem(modifier: Modifier = Modifier, questionSetting: QuestionSetting) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = SocialWorkReviewerIcons.Question,
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${questionSetting.numberOfQuestions} questions",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = SocialWorkReviewerIcons.AccessTime,
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${questionSetting.minutes} minutes",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnBoardingTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    CenterAlignedTopAppBar(
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
        modifier = modifier.testTag("onBoarding:centerAlignedTopAppBar"),
    )
}