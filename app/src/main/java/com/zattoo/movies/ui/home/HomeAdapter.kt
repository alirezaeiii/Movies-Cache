package com.zattoo.movies.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.databinding.ListItemMoviesBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var listMovie: List<Movie> = emptyList()

    fun setList(fragment: HomeFragment) {
        listMovie = fragment.movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMovie[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listMovie.size

    class ViewHolder private constructor(private val binding: ListItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.movies = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMoviesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}