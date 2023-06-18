package ru.xlwe.lesson2

interface Comparing<T> {
    fun same(item: T): Boolean
    fun sameContent(item: T): Boolean
    fun changePayload(item: T): Any = Any()
}