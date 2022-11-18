package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_playlist.*

class playlistActivity : AppCompatActivity() {
    private  lateinit var  binding : ActivityPlaylistBinding
    private  lateinit var  PlayListsAdapter : PlayListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        val tempList = ArrayList<String>()
        tempList.add("salam")
        tempList.add("amir")
        tempList.add("narges")
        tempList.add("saleh")
        tempList.add("armin")
        tempList.add("behnoosh")
        initializeRecyclerView(tempList)
        backFromPlayList.setOnClickListener{
            finish()
        }
    }

    private fun initializeRecyclerView(tempList : ArrayList<String>){
        playlists.setHasFixedSize(true)
        playlists.setItemViewCacheSize(13)
        playlists.layoutManager = GridLayoutManager(this@playlistActivity , 2)
        PlayListsAdapter = PlayListViewAdapter(this, tempList)
        playlists.adapter = PlayListsAdapter
    }

}