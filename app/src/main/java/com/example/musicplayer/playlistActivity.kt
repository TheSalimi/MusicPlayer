package com.example.musicplayer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import com.example.musicplayer.databinding.AddPlaylistDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_playlist.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class playlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlayListViewAdapter

    companion object {
        var musicPlayList: MusicPlayList = MusicPlayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecyclerView(musicPlayList.ref)
        backFromPlayList.setOnClickListener {
            finish()
        }
        addPlayListBtn.setOnClickListener { customAlertDialog() }
    }

    private fun initializeRecyclerView(tempList: ArrayList<PlayList>) {
        playlists.setHasFixedSize(true)
        playlists.setItemViewCacheSize(13)
        playlists.layoutManager = GridLayoutManager(this@playlistActivity, 2)
        adapter = PlayListViewAdapter(this, musicPlayList.ref)
        playlists.adapter = adapter
    }

    private fun customAlertDialog() {
        val customDialog = LayoutInflater.from(this@playlistActivity)
            .inflate(R.layout.add_playlist_dialog, binding.root, false)

        val binder = AddPlaylistDialogBinding.bind(customDialog)

        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )

        builder.setView(customDialog)
            .setTitle("Playlist Details")
            .setPositiveButton("Add") { dialog, _ ->
                val playListName = binder.playListNameEt.text
                if (playListName != null) {
                    if (playListName.isNotEmpty()) {
                        addPlayList(playListName.toString())
                    }
                }
                dialog.dismiss()
            }.show()
    }

    private fun addPlayList(name: String) {
        var playListExists = false

        for (i in musicPlayList.ref) {
            if (name.equals(i.name)) {
                playListExists = true
                break
            }
        }

        if(playListExists) Toast.makeText(this , "playlist exist" , Toast.LENGTH_SHORT).show()
        else{
            val tempPlayList = PlayList()
            tempPlayList.name = name
            tempPlayList.playList = ArrayList()
            val calender =  java.util.Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyy" , Locale.ENGLISH)
            tempPlayList.createdOn = sdf.format(calender)
            musicPlayList.ref.add(tempPlayList)
            adapter.refreshPlaylist()
        }
    }

}