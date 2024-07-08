package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.data.repository.ChoiceRepository
import com.android.socialworkreviewer.core.model.Choice
import javax.inject.Inject

class UpdateChoiceUseCase @Inject constructor(private val choiceRepository: ChoiceRepository) {
    suspend operator fun invoke(choice: Choice) {
        if (choice.question.correctChoices.size > 1) {
            multipleChoices(choice = choice)
        } else {
            singleChoice(choice = choice)
        }
    }

    private suspend fun multipleChoices(choice: Choice) {
        if (choice in choiceRepository.selectedChoices) {
            choiceRepository.deleteChoice(choice)
        } else {
            choiceRepository.addChoice(choice)
        }
    }

    private suspend fun singleChoice(choice: Choice) {
        choiceRepository.selectedChoices.forEach { selectedChoice ->
            if (choice.question == selectedChoice.question) {
                choiceRepository.deleteChoice(selectedChoice)
            }
        }

        choiceRepository.addChoice(choice)
    }
}