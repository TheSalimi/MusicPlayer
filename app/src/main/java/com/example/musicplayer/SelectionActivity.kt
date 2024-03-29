package com.example.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.MusicAdapter
import com.example.musicplayer.databinding.ActivitySelectionBinding
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectionBinding
    private lateinit var adapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()
        binding.addToPlaylistBtn.setOnClickListener {finish()}

        //for search view
        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?)=true

            override fun onQueryTextChange(newText: String?): Boolean {
                MainActivity.musicListSearch = ArrayList()
                if(newText!=null){
                    val userInput= newText.lowercase()
                    for(song in MainActivity.musicList){
                        if(song.title.lowercase().contains(userInput)){
                            MainActivity.musicListSearch.add(song)
                        }
                    }
                    MainActivity.search = true
                    adapter.updateMusicList(searchList = MainActivity.musicListSearch)
                }
                return true
            }
        })
    }

    private fun setRecyclerView(){
        selectionRV.setItemViewCacheSize(100)
        selectionRV.setHasFixedSize(true)
        selectionRV.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter( this , MainActivity.musicList , selectionActivity = true)
        selectionRV.adapter = adapter
    }
}