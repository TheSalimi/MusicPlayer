package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityFavoritesBinding
import com.example.musicplayer.databinding.FavoriteViewBinding
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.activity_player.view.*
import kotlinx.android.synthetic.main.favorite_view.*
import kotlinx.android.synthetic.main.favorite_view.view.*

class FavoritesActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityFavoritesBinding
    private lateinit var adapter : FavoriteAdaptor

    companion object{
        var favorieSongs : ArrayList<Music> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backButton.setOnClickListener { finish() }
        favoritesList.setHasFixedSize(true)
        favoritesList.setItemViewCacheSize(13)
        favoritesList.layoutManager = GridLayoutManager(this , 2)
        adapter = FavoriteAdaptor(this, favorieSongs)
        favoritesList.adapter = adapter
        if(favoritesList.size!=0) songNameFav.isSelected = true
        if(favorieSongs.size <  1) shuffleButtonFav.visibility = View.INVISIBLE
        shuffleButtonFav.setOnClickListener {shuffle()}
    }

    private fun shuffle(){
        val intent = Intent(this@FavoritesActivity, PlayerActivity::class.java)
        intent.putExtra("index", 0)
        intent.putExtra("class", "FavoriteShuffle")
        startActivity(intent)
    }
}