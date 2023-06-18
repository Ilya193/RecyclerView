package ru.xlwe.lesson2

import android.os.Bundle

sealed class Joke(
    private val text: String
) : Comparing<Joke> {

    override fun sameContent(item: Joke): Boolean = this == item

    fun setName(data: Mapper<String>) {
        data.map(text)
    }

    open fun changeLike() {}
    open fun changeDescription() {}
    open fun isHide(): Boolean = false
    open fun isLike(): Boolean = false
    open fun setDescription(data: Mapper<String>) {}
    override fun changePayload(item: Joke): Any {
        return Any()
    }

    data class Base(
        private val id: Int,
        private val text: String,
        private var isLike: Boolean = false,
        private val description: String,
        private var isHideDescription: Boolean = false
    ) : Joke(text) {
        override fun same(item: Joke): Boolean {
            return item is Base && id == item.id
        }

        override fun changePayload(item: Joke): Any {
            val bundle = Bundle()
            if (item is Base && isLike != item.isLike)
                bundle.putBoolean("favorite", true)
            if (item is Base && isHideDescription != item.isHideDescription)
                bundle.putBoolean("description", true)
            return bundle
        }

        override fun setDescription(data: Mapper<String>) {
            data.map(description)
        }

        override fun changeLike() {
            isLike = !isLike
        }

        override fun changeDescription() {
            isHideDescription = !isHideDescription
        }

        override fun isLike(): Boolean = isLike

        override fun isHide(): Boolean = isHideDescription
    }

    data class Info(
        private val text: String = "Каждая 5"
    ) : Joke(text) {
        override fun same(item: Joke): Boolean {
            return item is Info && text == item.text
        }
    }
}

