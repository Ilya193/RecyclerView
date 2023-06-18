package ru.xlwe.recyclerview

interface Listeners {
    fun onDelete(position: Int)
    fun onLiked(position: Int)
    fun onClick(position: Int)
}