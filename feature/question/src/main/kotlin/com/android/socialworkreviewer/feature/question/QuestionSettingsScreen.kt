package com.android.socialworkreviewer.feature.question

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.designsystem.component.DynamicAsyncImage
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.QuestionSetting

@Composable
internal fun QuestionSettingsScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    imageUrl: String?,
    questionSettings: List<QuestionSetting>,
    onGetQuestions: (Int, Int) -> Unit,
) {
    val quickModes = listOf("Yes", "No")

    val (selectedQuickSetting, onQuickSettingSelected) = remember { mutableStateOf(questionSettings[0]) }

    val (selectedQuickMode, onQuickModeSelected) = remember { mutableStateOf(quickModes[1]) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            onGetQuestions(
                selectedQuickSetting.numberOfQuestions, selectedQuickSetting.minutes
            )
        }) {
            Icon(imageVector = SocialWorkReviewerIcons.Question, contentDescription = "")
        }
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            ImageBanner(
                modifier = modifier
                    .height(200.dp)
                    .fillMaxWidth(), imageUrl = imageUrl
            )

            FirstQuestion(
                questionSettings = questionSettings,
                selectedQuickSetting = selectedQuickSetting,
                onQuickSettingSelected = onQuickSettingSelected
            )

            SecondQuestion(
                quickModes = quickModes,
                selectedQuickMode = selectedQuickMode,
                onQuickModeSelected = onQuickModeSelected
            )
        }
    }
}

@Composable
private fun ImageBanner(modifier: Modifier = Modifier, imageUrl: String?) {
    DynamicAsyncImage(
        model = imageUrl, contentDescription = "categoryImage", modifier = modifier
    )
}

@Composable
private fun FirstQuestion(
    modifier: Modifier = Modifier,
    questionSettings: List<QuestionSetting>,
    selectedQuickSetting: QuestionSetting,
    onQuickSettingSelected: (QuestionSetting) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        modifier = Modifier.padding(10.dp),
        text = "How many questions would you like to take?",
        style = MaterialTheme.typography.headlineLarge
    )

    Spacer(modifier = Modifier.height(10.dp))

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            Modifier.selectableGroup()
        ) {
            questionSettings.forEach { questionSetting ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (questionSetting == selectedQuickSetting),
                            onClick = { onQuickSettingSelected(questionSetting) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = (questionSetting == selectedQuickSetting), onClick = null
                    )
                    Text(
                        text = "${questionSetting.numberOfQuestions} Questions in ${questionSetting.minutes} minutes",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondQuestion(
    modifier: Modifier = Modifier,
    quickModes: List<String>,
    selectedQuickMode: String,
    onQuickModeSelected: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        modifier = Modifier.padding(10.dp),
        text = "Do you want to play quickly?",
        style = MaterialTheme.typography.headlineLarge
    )

    Spacer(modifier = Modifier.height(10.dp))

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            Modifier.selectableGroup()
        ) {
            quickModes.forEach { quickMode ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (quickMode == selectedQuickMode),
                            onClick = { onQuickModeSelected(quickMode) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (quickMode == selectedQuickMode), onClick = null
                    )

                    Text(
                        text = quickMode,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}