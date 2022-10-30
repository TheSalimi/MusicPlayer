package com.example.musicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.myHolder>() {
    class myHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val titile = binding.musicName
        val subTitle = binding.subTitle
        val image = binding.musicImage
        val length = binding.musicLength
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.myHolder {
        return myHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.titile.text = musicList[position].title
        holder.subTitle.text = musicList[position].album
        holder.length.text = musicList[position].duration.toString()

        Glide.with(context).load(musicList[position].artUri).into(holder.image)
    }
    override fun getItemCount(): Int = musicList.size

}