package com.example.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.*
import com.example.musicplayer.databinding.MusicViewBinding

class MusicAdapter(
    private val context: Context,
    private var musicList: ArrayList<Music>,
    private var playListDetails: Boolean = false,
    private var selectionActivity: Boolean = false
) : RecyclerView.Adapter<MusicAdapter.myHolder>() {
    class myHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val titile = binding.musicName
        val subTitle = binding.subTitle
        val image = binding.musicImage
        val length = binding.musicLength
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        return myHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.titile.text = musicList[position].title
        holder.subTitle.text = musicList[position].album
        holder.length.text = formatDuration(musicList[position].duration)

        Glide.with(context).load(musicList[position].artUri).into(holder.image)

        when {
            playListDetails -> {
                holder.root.setOnClickListener {
                    sendIntent("PlayListDetailsAdapter", position)
                }
            }
            selectionActivity ->{
                holder.root.setOnClickListener {
                    if(addSong(musicList[position]))
                        holder.root.setBackgroundColor(ContextCompat.getColor(context ,
                            R.color.light_gray
                        ))
                    else
                        holder.root.setBackgroundColor(ContextCompat.getColor(context  ,
                            R.color.white
                        ))
                }
            }
            else -> {
                holder.root.setOnClickListener {
                    when {
                        MainActivity.search -> sendIntent("MusicAdapterSearch", position)
                        musicList[position].id == PlayerActivity.nowPlayingId -> sendIntent(
                            "NowPlaying",
                            PlayerActivity.songPosition
                        )
                        else -> sendIntent("MusicAdapter", position)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = musicList.size

    fun updateMusicList(searchList: ArrayList<Music>) {
        musicList = ArrayList()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    private fun addSong (song : Music) : Boolean {
        playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList.forEachIndexed{ index, music ->
            if(song.id ==  music.id){
                playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList.removeAt(index)
                return false
            }
        }
        playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList.add(song)
        return true
    }

    fun refreshPLayList(){
        musicList = ArrayList()
        musicList = playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList
        notifyDataSetChanged()
    }
}