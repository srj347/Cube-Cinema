package com.example.cubecinema.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cubecinema.R
import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.model.Movie

class MovieBookmarkAdapter(
    private val context: Context,
    private val movieBookmarkList: ArrayList<Movie>
) :
    RecyclerView.Adapter<MovieBookmarkAdapter.MovieBookmarkViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieBookmarkViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie_bookmark, parent, false)
        return MovieBookmarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieBookmarkList.size
    }

    override fun onBindViewHolder(holder: MovieBookmarkViewHolder, position: Int) {
        val movie = movieBookmarkList[position]
        holder.bind(position)
    }

    fun removeMovie(position: Int) {
        movieBookmarkList.removeAt(position)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    inner class MovieBookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        val movieBookmarkDeleteIb: ImageButton =
            itemView.findViewById(R.id.movie_bookmark_remove_ib)


        fun bind(position: Int) {
            val movie = movieBookmarkList[position]
            movieTitle.text = movie.title
            Glide.with(context)
                .load(NetworkModule.POSTER_BASE_URL + movie.posterPath)
                .into(moviePoster)

            // attaching onitem click listener
            movieBookmarkDeleteIb.setOnClickListener {
                itemClickListener?.onItemClick(position, R.id.movie_bookmark_remove_ib)
            }
        }

    }
}