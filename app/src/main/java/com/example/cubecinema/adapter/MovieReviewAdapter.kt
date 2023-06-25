package com.example.cubecinema.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cubecinema.R
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.model.MovieReview
import com.example.cubecinema.ui.activity.MovieActivity
import com.example.cubecinema.utils.DateUtil

class MovieReviewAdapter(
    private val context: Context, private val reviewsList: ArrayList<MovieReview>
) : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>() {

    private var itemClickListener: ItemClickListener? = null
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        // get screen width
        val displayMetrics = DisplayMetrics()
        (context as MovieActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        val view = LayoutInflater.from(context).inflate(R.layout.item_movie_review, parent, false)
        return MovieReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        val movieReview = reviewsList[position]

        val itemWidth = (screenWidth / 1.22).toInt()
        val lp = holder.itemView.layoutParams
        lp.height = lp.height
        lp.width = itemWidth
        holder.itemView.layoutParams = lp

        holder.bind(movieReview)
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    inner class MovieReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position, -1)
                }
            }
        }

        val movieReviewAuthorTv: TextView = itemView.findViewById(R.id.movie_review_author_tv)

        //        val movieReviewAvatarIm: ImageView = itemView.findViewById(R.id.movie_review_avatar_iv)
        val movieRatingTv: TextView = itemView.findViewById(R.id.movie_review_rating_tv)
        val movieReviewContentTv: TextView = itemView.findViewById(R.id.movie_review_content_tv)
        val movieReviewCreatedAt: TextView = itemView.findViewById(R.id.movie_review_date_tv)
        val noReviewYetTv: TextView = itemView.findViewById(R.id.no_reviews_yet_tv)

        fun bind(movieReview: MovieReview) {
            movieReviewAuthorTv.text = movieReview.authorDetails?.author
            movieReviewContentTv.text = movieReview.content
            movieRatingTv.text = "${movieReview.authorDetails?.rating ?: 5}/10"
            movieReviewCreatedAt.text =
                DateUtil.convertYYYYMMDDToReadableFormat(movieReview.createdAt!!)
            /*
                Glide.with(context)
                    .load(NetworkModule.POSTER_BASE_URL + movieReview.authorDetails?.profilePath)
                    .into(movieReviewAvatarIm)
                    */
        }
    }
}