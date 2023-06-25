package com.example.cubecinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cubecinema.R
import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.model.MovieCast

class MovieCastAdapter(
    private val context: Context,
    private val castList: ArrayList<MovieCast>
) :
    RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = castList[position]
        holder.bind(cast)
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position, -1)
                }
            }
        }

        val castNameTv: TextView = itemView.findViewById(R.id.movie_cast_name)
        val castProfilePicIm: ImageView = itemView.findViewById(R.id.movie_cast_iv)
        val castDepartmentTv: TextView = itemView.findViewById(R.id.movie_cast_department)

        fun bind(cast: MovieCast) {
            castNameTv.text = cast.name
            castDepartmentTv.text = cast.department
            if (cast.profilePath == null) {
                castProfilePicIm.setBackgroundResource(R.drawable.ic_avatar)
            }
            Glide.with(context)
                .load(NetworkModule.POSTER_BASE_URL + cast.profilePath)
                .into(castProfilePicIm)
        }
    }
}