package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivitySelectionBinding
import kotlinx.android.synthetic.main.activity_play_list_details.*
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectionBinding
    private lateinit var adapter : MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
    }

    private fun setRecyclerView(){
        selectionRV.setItemViewCacheSize(10)
        selectionRV.setHasFixedSize(true)
        selectionRV.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList)
        selectionRV.adapter = adapter
    }
}