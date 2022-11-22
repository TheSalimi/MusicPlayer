package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.ActivityPlayListDetailsBinding
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import kotlinx.android.synthetic.main.activity_play_list_details.*

class PlayListDetails : AppCompatActivity() {
    companion object{
        var currentPlayListPosition : Int = -1
    }

    lateinit var binding : ActivityPlayListDetailsBinding
    lateinit var adapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayListDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        currentPlayListPosition = intent.extras?.get("index") as Int
        musics.setItemViewCacheSize(10)
        musics.setHasFixedSize(true)
        playlistActivity.musicPlayList.ref[currentPlayListPosition].playList.addAll(MainActivity.musicList)
        musics.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , playlistActivity.musicPlayList.ref[currentPlayListPosition].playList , true)
        musics.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.playListName.text = playlistActivity.musicPlayList.ref[currentPlayListPosition].name
        if(adapter.itemCount>0){
            Glide.with(this)
                .load(playlistActivity.musicPlayList.ref[currentPlayListPosition].playList[0].artUri)
                .into(binding.playListImageInDetails)
            binding.shuffleInDetils.visibility = View.VISIBLE
        }
    }
}