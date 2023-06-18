package ru.xlwe.lesson2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.xlwe.lesson2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Listeners {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: JokeViewModel by viewModels()
    private val adapter = JokeAdapter(this)

    //recyclerview inside recyclerview
    /*private val viewModel: MainViewModel by viewModels()
    private val adapter = MainAdapter(this)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.list.adapter = adapter
        binding.list.setHasFixedSize(true)
        binding.list.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        //binding.list.itemAnimator = null

        /*viewModel.observe(this) {
            adapter.submitList(it.map { item ->
                if (item is Base.Item) item.copy() else Base.Item((100..200).random(), listOf())
            })
        }*/

        viewModel.observe(this) {
            adapter.submitList(it.map { joke ->
                if (joke is Joke.Base) joke.copy() else Joke.Info()
            })
        }
    }

    override fun onDelete(position: Int) {
        if (position >= 0)
            viewModel.delete(position)
    }

    override fun onLiked(position: Int) {
        viewModel.setLike(position)
    }

    override fun onClick(position: Int) {
        viewModel.hideDescription(position)
    }
}