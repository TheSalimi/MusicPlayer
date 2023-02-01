package com.example.musicplayer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.MusicAdapter
import com.example.musicplayer.databinding.ActivityPlayListDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_play_list_details.*

class PlayListDetails : AppCompatActivity() {
    companion object{
        var currentPlayListPosition : Int = -1
    }

    lateinit var binding : ActivityPlayListDetailsBinding
    lateinit var adapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayListDetailsBinding.inflate(layoutInflater)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        currentPlayListPosition = intent.extras?.get("index") as Int
        try {
            playlistActivity.musicPlayList.ref[currentPlayListPosition].playList =
                checkPlayList(playlistActivity.musicPlayList.ref[currentPlayListPosition].playList)
        }
        catch (e: Exception){}
        musics.setItemViewCacheSize(10)
        musics.setHasFixedSize(true)
        musics.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , playlistActivity.musicPlayList.ref[currentPlayListPosition].playList , true)
        binding.musics.adapter = adapter

        val playListName = playlistActivity.musicPlayList.ref[currentPlayListPosition].name
        actionBar?.title = playListName;
        supportActionBar?.title = playListName;

        binding.addBtn.setOnClickListener {
            startActivity(Intent( this , SelectionActivity::class.java))
        }

        binding.removeBtn.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Remove")
                .setMessage("Do you want to remove all songs from playlist?")
                .setPositiveButton("Yes") { dialog, _ ->
                    playlistActivity.musicPlayList.ref[currentPlayListPosition].playList.clear()
                    adapter.refreshPLayList()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
    }

    override fun onResume() {
        super.onResume()
        if(adapter.itemCount>0){
            Glide.with(this)
                .load(playlistActivity.musicPlayList.ref[currentPlayListPosition].playList[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_splash).centerCrop())
                .into(binding.playListImageInDetails)
        }
        adapter.notifyDataSetChanged()

        val editor = getSharedPreferences( "FAVORITES" , MODE_PRIVATE).edit()
        val jsonStringPlayList = GsonBuilder().create().toJson(playlistActivity.musicPlayList)
        editor.putString("MusicPlayList" , jsonStringPlayList)
        editor.apply()

        val playListName = playlistActivity.musicPlayList.ref[currentPlayListPosition].name
        actionBar?.title = playListName;
        supportActionBar?.title = playListName;
    }
}