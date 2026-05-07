package com.example.firebaseornek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class WallpaperAdapter(
    private val wallpapers: List<String>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {

    class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wallpaperImageView: ImageView = itemView.findViewById(R.id.wallpaperImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallpaper_item, parent, false)

        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val imageUrl = wallpapers[position]

        Picasso.get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(holder.wallpaperImageView)

        holder.itemView.setOnClickListener {
            listener(imageUrl)
        }
    }

    override fun getItemCount(): Int {
        return wallpapers.size
    }
}