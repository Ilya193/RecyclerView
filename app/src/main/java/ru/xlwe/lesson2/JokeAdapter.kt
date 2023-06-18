package ru.xlwe.lesson2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ru.xlwe.lesson2.databinding.ItemInfoBinding
import ru.xlwe.lesson2.databinding.JokeBinding

class JokeAdapter(
    private val listeners: Listeners
) : BaseAdapter<Joke, BaseViewHolder<Joke>>() {

    override fun getItemViewType(position: Int): Int {
        val joke = getItem(position)
        if (joke is Joke.Base) return VIEW_TYPE_JOKE
        return VIEW_TYPE_INFO
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<Joke>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            if (bundle.size() != 0) {
                val favorite = bundle.getBoolean("favorite")
                if (favorite) holder.bindFavorite()
                val description = bundle.getBoolean("description")
                if (description) holder.bindDescription(getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Joke> {
        return when (viewType) {
            VIEW_TYPE_JOKE -> JokeViewHolder(
                JokeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), listeners
            )

            else -> InfoViewHolder(
                ItemInfoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    class InfoViewHolder(
        view: ItemInfoBinding
    ) : BaseViewHolder<Joke>(view.root) {
        private val text: CustomTextView = view.root.findViewById(R.id.text)

        override fun bind(joke: Joke, position: Int) {
            joke.setName(text)
        }
    }

    class JokeViewHolder(
        view: JokeBinding, private val listeners: Listeners
    ) : BaseViewHolder<Joke>(view.root) {
        private val text: CustomTextView = view.root.findViewById(R.id.text)
        private val delete: ImageView = view.root.findViewById(R.id.delete)
        private val description: CustomTextView = view.root.findViewById(R.id.description)
        private val isLike: ImageView = view.root.findViewById(R.id.like)

        override fun bind(joke: Joke, position: Int) {
            joke.setName(text)
            joke.setDescription(description)
            isLike.setOnClickListener { listeners.onLiked(adapterPosition) }
            delete.setOnClickListener { listeners.onDelete(adapterPosition) }
            text.setOnClickListener {
                listeners.onClick(adapterPosition)
            }
        }

        override fun bindFavorite() {
            if (!isLike.isSelected) {
                isLike.setBackgroundResource(R.drawable.favorite)
                isLike.isSelected = !isLike.isSelected
            } else {
                isLike.setBackgroundResource(R.drawable.favorite_border)
                isLike.isSelected = !isLike.isSelected
            }
        }

        override fun bindDescription(joke: Joke) {
            description.visibility = if (joke.isHide()) View.VISIBLE else View.GONE
        }
    }

    private companion object {
        const val VIEW_TYPE_JOKE = 1
        const val VIEW_TYPE_INFO = 2
    }
}