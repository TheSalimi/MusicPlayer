package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityFeedBackBinding
import com.example.musicplayer.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Setting"
        setContentView(binding.root)
    }
}