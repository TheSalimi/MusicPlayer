package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityFavoritesBinding
import com.example.musicplayer.databinding.FavoriteViewBinding
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoritesActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityFavoritesBinding
    private lateinit var adapter : FavoriteAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val tempList = ArrayList<String>()
        tempList.add("song 1")
        tempList.add("song 2")
        tempList.add("song 3")
        tempList.add("song 4")
        tempList.add("song 5")
        tempList.add("song 6")
        tempList.add("song 7")
        setContentView(binding.root)
        backButton.setOnClickListener { finish() }
        favoritesList.setHasFixedSize(true)
        favoritesList.setItemViewCacheSize(13)
        favoritesList.layoutManager = GridLayoutManager(this , 2)
        adapter = FavoriteAdaptor(this, tempList)
        favoritesList.adapter = adapter
    }
}