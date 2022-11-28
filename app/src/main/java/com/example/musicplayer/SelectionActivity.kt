package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
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
        adapter = MusicAdapter( this , MainActivity.musicList , selectionActivity = true)
        selectionRV.adapter = adapter
    }

    private fun setSearchView(){
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
}