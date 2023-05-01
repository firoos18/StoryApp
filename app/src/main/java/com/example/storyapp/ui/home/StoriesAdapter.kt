package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.ui.detail.StoryDetailActivity

class StoriesAdapter(
    private val listPhotoUrl : List<String>,
    private val listName : List<String>,
    private val listId : List<String>
) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(username : String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView : ImageView = view.findViewById(R.id.iv_item_photo)
        val tvName : TextView = view.findViewById(R.id.tv_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false))

    override fun getItemCount(): Int = listName.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listName[position]
        Glide.with(holder.imgView).load(listPhotoUrl[position]).into(holder.imgView)

        holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(listName[holder.adapterPosition]) }
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intentDetail.putExtra("id", listId[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

}