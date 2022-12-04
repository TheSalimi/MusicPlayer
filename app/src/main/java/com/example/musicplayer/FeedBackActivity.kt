package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityFeedBackBinding
import com.example.musicplayer.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_feed_back.*

class FeedBackActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityFeedBackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val feedbackMsg = feedbackText.text.toString() + "\n" + feedbackEmail.text.toString()
        val subject  = feedbackTopic.text.toString()

        binding.feedbackSend.setOnClickListener {
            Toast.makeText(this , feedbackMsg , Toast.LENGTH_LONG).show()
        }

        supportActionBar?.title = "Feedback"
    }
}