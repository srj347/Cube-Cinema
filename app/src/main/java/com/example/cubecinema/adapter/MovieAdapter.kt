package com.example.cubecinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cubecinema.R
import com.example.cubecinema.adapter.utils.MovieDiffCallback
import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.model.Movie
import com.example.cubecinema.utils.DateUtil

class MovieAdapter(
    private val context: Context,
    private val movieList: ArrayList<Movie>
) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    fun updateMovieList(newMovieList: ArrayList<Movie>) {
        val diffCallback = MovieDiffCallback(movieList, newMovieList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        movieList.clear()
        movieList.addAll(newMovieList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position, -1)
                }
            }
        }

        val movieTitle: TextView = itemView.findViewById(R.id.movie_title_tv)
        val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster_iv)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.movie_release_date_tv)
        val bookMovieBtn: ImageButton = itemView.findViewById(R.id.book_movie_btn)


        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            movieReleaseDate.text = DateUtil.convertYYYYMMDDToReadableFormat(movie.releaseDate!!)
            Glide.with(context)
                .load(NetworkModule.POSTER_BASE_URL + movie.backdropPath)
                .into(moviePoster)
        }

    }
}