package com.example.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.Music
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.databinding.FavoriteViewBinding

class FavoriteAdaptor(private val context: Context, private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<FavoriteAdaptor.myHolder>() {
    class myHolder(binding: FavoriteViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val titile = binding.songNameFav
        val image = binding.songImageFav
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        return myHolder(FavoriteViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.titile.text = musicList[position].title
        holder.titile.isSelected = true
        Glide.with(context).load(musicList[position].artUri).into(holder.image)
        holder.root.setOnClickListener{
             sendIntent("FavoriteAdapter" , position)
        }
    }

    override fun getItemCount(): Int = musicList.size

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
}