package ru.xlwe.lesson2

interface Listeners {
    fun onDelete(position: Int)
    fun onLiked(position: Int)
    fun onClick(position: Int)
}