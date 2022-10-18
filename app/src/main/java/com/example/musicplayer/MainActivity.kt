package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.musicplayer.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shuffleButton.setOnClickListener{
            Toast.makeText(this , "hey " , Toast.LENGTH_SHORT).show()
        }

        favoriteButton.setOnClickListener {
            val intent = Intent(this@MainActivity , FavoritesActivity::class.java)
            startActivity(intent)
        }

        playlistButton.setOnClickListener {
            val intent = Intent(this@MainActivity , playlistActivity::class.java)
            startActivity(intent)
        }
    }
}