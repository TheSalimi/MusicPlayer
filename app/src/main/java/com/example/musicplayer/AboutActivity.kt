package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityAboutBinding
import com.example.musicplayer.databinding.ActivityFeedBackBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        setContentView(R.layout.activity_about)
    }
}