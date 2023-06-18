package ru.xlwe.recyclerview

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Comparing<T>, E : BaseViewHolder<T>> :
    ListAdapter<T, E>(DiffUtilCallback<T>()) {
    override fun onBindViewHolder(holder: E, position: Int) {
        holder.bind(getItem(position), position)
    }
}

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(joke: T, position: Int)
    open fun bindFavorite() {}
    open fun bindDescription(joke: T) {}
}