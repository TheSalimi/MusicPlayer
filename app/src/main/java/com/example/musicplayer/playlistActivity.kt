package com.example.musicplayer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_playlist.*

class playlistActivity : AppCompatActivity() {
    private  lateinit var  binding : ActivityPlaylistBinding
    private  lateinit var  PlayListsAdapter : PlayListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        addPlayListBtn.setOnClickListener { customAlertDialog() }
    }

    private fun initializeRecyclerView(tempList : ArrayList<String>){
        playlists.setHasFixedSize(true)
        playlists.setItemViewCacheSize(13)
        playlists.layoutManager = GridLayoutManager(this@playlistActivity , 2)
        PlayListsAdapter = PlayListViewAdapter(this, tempList)
        playlists.adapter = PlayListsAdapter
    }

    private fun customAlertDialog(){
        val customDialog = LayoutInflater.from(this@playlistActivity)
            .inflate(R.layout.add_playlist_dialog , binding.root , false)

        val builder = MaterialAlertDialogBuilder(this  , R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)

        builder.setView(customDialog)
            .setTitle("Playlist Details")
            .setPositiveButton("Add"){ dialog , _ ->
                dialog.dismiss()
            }.show()
    }

}