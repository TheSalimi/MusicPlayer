package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivityPlayListDetailsBinding
import com.example.musicplayer.databinding.ActivityPlaylistBinding

class PlayListDetails : AppCompatActivity() {

    companion object{
        var currentPlayListPosition : Int = -1
    }

    lateinit var binding : ActivityPlayListDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayListDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        currentPlayListPosition = intent.extras?.get("index") as Int
    }
}