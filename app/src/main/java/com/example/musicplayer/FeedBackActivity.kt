package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityFeedBackBinding
import com.example.musicplayer.databinding.ActivityMainBinding

class FeedBackActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityFeedBackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Feedback"
    }
}