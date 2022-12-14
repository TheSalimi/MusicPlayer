package com.example.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.*
import com.example.musicplayer.databinding.PlaylistViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayListViewAdapter(private val context: Context, private var playLists_list: ArrayList<PlayList>) :
    RecyclerView.Adapter<PlayListViewAdapter.myHolder>() {
    class myHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val titile = binding.playListNameTV
        val image = binding.playListImage
        val deleteBtn = binding.playListDeleteBtn
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        return myHolder(PlaylistViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.titile.text = playLists_list[position].name
        holder.titile.isSelected = true
        holder.deleteBtn.setOnClickListener{
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(playLists_list[position].name)
                .setMessage("Do you want to delete playlist?")
                .setPositiveButton("Yes") { dialog, _ ->
                    playlistActivity.musicPlayList.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }

        holder.root.setOnClickListener {
            val intent = Intent(context , PlayListDetails::class.java)
            intent.putExtra("index" , position)
            ContextCompat.startActivity(context , intent , null);
        }

        if(playlistActivity.musicPlayList.ref[position].playList.size > 0){
            Glide.with(context)
                .load(playlistActivity.musicPlayList.ref[position].playList[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.splash_screen).centerCrop())
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int = playLists_list.size

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    fun refreshPlaylist(){
        playLists_list = ArrayList()
        playLists_list.addAll(playlistActivity.musicPlayList.ref)
        notifyDataSetChanged()
    }
}