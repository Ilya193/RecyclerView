package ru.xlwe.lesson2

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeViewModel : ViewModel() {
    private val liveData = MutableLiveData<List<Joke>>()
    private val list = mutableListOf<Joke>()

    init {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        var resString: String
        var count = 1

        for (i in 0..10) {
            resString = (1..20).map { charset.random() }.joinToString("")
            if (count++ % 5 == 0)
                list.add(Joke.Info())
            else
                list.add(
                    Joke.Base(
                        id = i,
                        text = resString,
                        description = "Gravida definitiones accommodare nam hac. " +
                                "Consectetur ne necessitatibus convallis mazim solet. No lorem suavitate nulla " +
                                "mnesarchum pulvinar netus.",
                    )
                )
        }

        liveData.value = list
    }

    fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<List<Joke>>) {
        liveData.observe(lifecycleOwner, observer)
    }

    fun setLike(position: Int) {
        list[position].changeLike()
        liveData.value = list
    }

    fun hideDescription(position: Int) {
        list[position].changeDescription()
        liveData.value = list
    }

    fun delete(position: Int) {
        deleteItem(position)
    }

    private fun deleteItem(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        var delPosition = position
        var checkInfo = false
        val currentPosition = position + 1

        for (i in currentPosition until list.size) {
            if (list[i] !is Joke.Info) {
                if (checkInfo) {
                    list[delPosition] = list[i]
                    delPosition += 2
                    checkInfo = false
                } else {
                    list[delPosition] = list[i]
                    delPosition++
                }
            } else {
                if (i == list.size - 1) {
                    list[delPosition] = list[i]
                    list.removeAt(i)
                }
                checkInfo = true
            }
        }

        if (list[list.lastIndex] !is Joke.Info)
            list.removeAt(list.lastIndex)

        withContext(Dispatchers.Main) { liveData.value = list }
    }
}