package com.example.musicplayer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.MusicAdapter
import com.example.musicplayer.databinding.ActivityPlayListDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_play_list_details.*

class PlayListDetails : AppCompatActivity() {
    companion object{
        var currentPlayListPosition : Int = -1
    }

    lateinit var binding : ActivityPlayListDetailsBinding
    lateinit var adapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayListDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.shuffleInDetils.visibility = View.INVISIBLE
        binding.backFromPlayListDetails.setOnClickListener { finish() }

        currentPlayListPosition = intent.extras?.get("index") as Int
        musics.setItemViewCacheSize(10)
        musics.setHasFixedSize(true)
        musics.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , playlistActivity.musicPlayList.ref[currentPlayListPosition].playList , true)
        binding.musics.adapter = adapter
        binding.playListName.isSelected = true

        binding.shuffleInDetils.setOnClickListener {
            val intent = Intent(this , PlayerActivity::class.java)
            intent.putExtra("index" , 0)
            intent.putExtra("class" , "PlaylistDetailsShuffle")
            startActivity(intent)
        }

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
        binding.playListName.text = playlistActivity.musicPlayList.ref[currentPlayListPosition].name
        if(adapter.itemCount>0){
            Glide.with(this)
                .load(playlistActivity.musicPlayList.ref[currentPlayListPosition].playList[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_splash).centerCrop())
                .into(binding.playListImageInDetails)
            binding.shuffleInDetils.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
    }
}