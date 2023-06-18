package ru.xlwe.lesson2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.xlwe.lesson2.databinding.ItemTestBinding
import ru.xlwe.lesson2.databinding.ItemTestSubBinding

//recyclerview inside recyclerview

sealed class Base(
    private val id: Int,
) : Comparing<Base> {
    override fun same(item: Base): Boolean = id == item.id
    override fun sameContent(item: Base): Boolean = this == item

    open fun <T : Mapper<String>> mapText(data: T) {}
    open fun <T : Mapper<List<Base>>> mapList(data: T) {}

    data class SubItem(
        private val id: Int,
        private val text: String,
    ) : Base(id) {
        override fun <T : Mapper<String>> mapText(data: T) {
            data.map(text)
        }
    }

    data class Item(
        private val id: Int,
        private val list: List<SubItem>
    ) : Base(id) {
        override fun <T : Mapper<List<Base>>> mapList(data: T) {
            data.map(list)
        }
    }
}

class MainViewModel : ViewModel() {
    private val liveData = MutableLiveData<List<Base>>()
    private val list = mutableListOf<Base>()

    init {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        var resString: String
        val listSubItem = mutableListOf<Base.SubItem>()

        for (x in 0..10) {
            for (i in 0..10) {
                resString = (1..20).map { charset.random() }.joinToString("")
                listSubItem.add(Base.SubItem(id = i, text = resString))
            }

            val randomNum = (100..200).random()
            list.add(Base.Item(id = randomNum, listSubItem))
        }

        liveData.value = list
    }

    fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<List<Base>>) {
        liveData.observe(lifecycleOwner, observer)
    }

}

class MainDiffUtilCallback : DiffUtil.ItemCallback<Base>() {
    override fun areItemsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem.same(newItem)
    }

    override fun areContentsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem.sameContent(newItem)
    }
}

class MainAdapter(
    private val context: Context
) : ListAdapter<Base, MainAdapter.BaseViewHolder>(MainDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            ItemTestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BaseViewHolder(view: ItemTestBinding) :
        RecyclerView.ViewHolder(view.root) {
        private val recyclerView: RecyclerView = view.root.findViewById(R.id.rvSubList)
        private val subAdapter = SubAdapter()

        fun bind(item: Base) {
            recyclerView.adapter = subAdapter
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            item.mapList(subAdapter)
        }
    }
}

class SubDiffUtilCallback : DiffUtil.ItemCallback<Base>() {
    override fun areItemsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem.same(newItem)
    }

    override fun areContentsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem.sameContent(newItem)
    }
}

class SubAdapter : ListAdapter<Base, SubAdapter.BaseViewHolder>(SubDiffUtilCallback()),
    Mapper<List<Base>> {
    class BaseViewHolder(view: ItemTestSubBinding) : RecyclerView.ViewHolder(view.root) {
        private val content: CustomTextView = view.root.findViewById(R.id.content)

        fun bind(subItem: Base) {
            subItem.mapText(content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            ItemTestSubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun map(data: List<Base>) {
        submitList(data)
    }
}