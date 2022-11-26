package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.ActivityPlayListDetailsBinding
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import kotlinx.android.synthetic.main.activity_favorites.*
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

        binding.backFromPlayListDetails.setOnClickListener { finish() }

        currentPlayListPosition = intent.extras?.get("index") as Int
        musics.setItemViewCacheSize(10)
        musics.setHasFixedSize(true)
        playlistActivity.musicPlayList.ref[currentPlayListPosition].playList.addAll(MainActivity.musicList)
        playlistActivity.musicPlayList.ref[currentPlayListPosition].playList.shuffle()
        musics.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , playlistActivity.musicPlayList.ref[currentPlayListPosition].playList , true)
        binding.playListName.isSelected = true
        musics.adapter = adapter

        binding.shuffleInDetils.setOnClickListener {
            val intent = Intent(this , PlayerActivity::class.java)
            intent.putExtra("index" , 8)
            intent.putExtra("class" , "PlaylistDetailsShuffle")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.playListName.text = playlistActivity.musicPlayList.ref[currentPlayListPosition].name
        if(adapter.itemCount>0){
            Glide.with(this)
                .load(playlistActivity.musicPlayList.ref[currentPlayListPosition].playList[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_splash).centerCrop())
                .into(binding.playListImageInDetails)
            binding.shuffleInDetils.visibility = View.VISIBLE
        }
    }
}