package com.example.musicplayer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musicplayer.adapter.PlayListViewAdapter
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import com.example.musicplayer.databinding.AddPlaylistDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_playlist.*
import java.text.SimpleDateFormat
import java.util.*

class playlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlayListViewAdapter

    companion object {
        var musicPlayList: MusicPlayList = MusicPlayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.title = "Playlist";
        supportActionBar?.title = "Playlist";
        initializeRecyclerView(musicPlayList.ref)
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

        val builder = MaterialAlertDialogBuilder(this)

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
            }

        val customD = builder.create()
        customD.show()
        customD.getButton((AlertDialog.BUTTON_POSITIVE)).setTextColor(Color.RED)
    }

    private fun addPlayList(name: String) {
        var playListExists = false

        for (i in musicPlayList.ref) {
            if (name == i.name) {
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

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

}