package com.android.socialworkreviewer.core.model

data class Question(
    val question: String, val choices: Array<Choice>, val answer: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (question != other.question) return false
        if (!choices.contentEquals(other.choices)) return false
        if (answer != other.answer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + choices.contentHashCode()
        result = 31 * result + answer
        return result
    }
}
