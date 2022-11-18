package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.PlaylistViewBinding

class PlayListViewAdapter(private val context: Context, private var playLists_list: ArrayList<String>) :
    RecyclerView.Adapter<PlayListViewAdapter.myHolder>() {
    class myHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val titile = binding.playListNameTV
        val image = binding.playListImage
        val deleteBtn = binding.playListDeleteBtn
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewAdapter.myHolder {
        return myHolder(PlaylistViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.titile.text = playLists_list[position]
        holder.titile.isSelected = true
        holder.root.setOnClickListener{

        }
    }

    override fun getItemCount(): Int = playLists_list.size

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
}