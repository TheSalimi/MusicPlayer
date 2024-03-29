package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.MusicAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapeter: MusicAdapter

    companion object {
        lateinit var musicList: ArrayList<Music>
        lateinit var musicListSearch: ArrayList<Music>
        var search: Boolean = false
        var themeIndex: Int = 0
        val currentTheme = arrayOf(R.style.white, R.style.black)
        val currentThemeNav = arrayOf(R.style.whiteNav, R.style.blackNav)
        var sortOrder: Int = 0
        val sortingList = arrayOf(
            MediaStore.Audio.Media.DATE_ADDED + " DESC", MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE + " DESC"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeEditor = getSharedPreferences("THEMES", MODE_PRIVATE)
        themeIndex = themeEditor.getInt("themeIndex", 0)
        setTheme(currentThemeNav[themeIndex])
        requestRunTimePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (requestRunTimePermission()) {
            initialazation()
            getFromShardPrefrences()
        }
        shuffleButton.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "MainActivity")
            startActivity(intent)
        }

        favoriteButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
            startActivity(intent)
        }

        playlistButton.setOnClickListener {
            val intent = Intent(this@MainActivity, playlistActivity::class.java)
            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feedback -> startActivity(
                    Intent(
                        this@MainActivity,
                        FeedBackActivity::class.java
                    )
                )
                R.id.setting -> startActivity(
                    Intent(
                        this@MainActivity,
                        SettingActivity::class.java
                    )
                )
                R.id.about -> startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                R.id.exit -> {
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("Exit")
                        .setPositiveButton("Yes") { _, _ ->
                            exitApplication()
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
            true
        }
    }

    private fun requestRunTimePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                13
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Perimission granted", Toast.LENGTH_SHORT).show()
                initialazation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    13
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initialazation() {
        search = false
        val sortEditor = getSharedPreferences("SORTING" , MODE_PRIVATE)
        sortOrder = sortEditor.getInt("sortOrder",0)
        musicList = getAllAudio()
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(13)
        recyclerView.layoutManager = LinearLayoutManager(this)
        musicAdapeter = MusicAdapter(this, musicList)
        recyclerView.adapter = musicAdapeter
        totalSongs.text = "total songs : ${musicList.size}"
    }

    @SuppressLint("Range")
    private fun getAllAudio(): ArrayList<Music> {
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortingList[sortOrder], null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC =
                        cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)))
                    val artistC =
                        cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)))
                    val durationC =
                        cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)))
                    val pathC =
                        cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
                    val albumIdC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            .toString()

                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()
                    val music =
                        Music(idC, titleC, albumC, artistC, durationC.toLong(), pathC, artUriC)

                    val file = File(music.path)
                    if (file.exists()) {
                        tempList.add(music)
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return tempList
    }

    override fun onDestroy() {
        super.onDestroy()
        putToShardPrefrences()
        if (!PlayerActivity.isPlaying && PlayerActivity.musicService != null) {
            exitApplication()
        }
    }

    override fun onResume() {
        super.onResume()
        //for storing favorites data using shared prefrences
        putToShardPrefrences()
        //for sorting
        val sortEditor = getSharedPreferences("SORTING" , MODE_PRIVATE)
        val sortValue = sortEditor.getInt("sortOrder",0)
        if(sortOrder!=sortValue){
            sortOrder = sortValue
            musicList = getAllAudio()
            musicAdapeter.updateMusicList(musicList)
        }

    }

    private fun putToShardPrefrences() {
        val editor = getSharedPreferences("FAVORITES", MODE_PRIVATE).edit()
        val jsonString = GsonBuilder().create().toJson(FavoritesActivity.favorieSongs)
        editor.putString("FavoriteSongs", jsonString)

        val jsonStringPlayList = GsonBuilder().create().toJson(playlistActivity.musicPlayList)
        editor.putString("MusicPlayList", jsonStringPlayList)

        editor.apply()
    }

    private fun getFromShardPrefrences() {
        FavoritesActivity.favorieSongs = ArrayList()
        val editor = getSharedPreferences("FAVORITES", MODE_PRIVATE)
        val typeToken = object : TypeToken<ArrayList<Music>>() {}.type
        val jsonString = editor.getString("FavoriteSongs", null)
        if (jsonString != null) {
            val data: ArrayList<Music> = GsonBuilder().create().fromJson(jsonString, typeToken)
            FavoritesActivity.favorieSongs.addAll(data)
        }

        playlistActivity.musicPlayList = MusicPlayList()
        val jsonStringPlayList = editor.getString("MusicPlayList", null)
        if (jsonStringPlayList != null) {
            val dataPlayList: MusicPlayList =
                GsonBuilder().create().fromJson(jsonStringPlayList, MusicPlayList::class.java)
            playlistActivity.musicPlayList = dataPlayList
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView =
            menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                musicListSearch = ArrayList()
                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in musicList) {
                        if (song.title.lowercase().contains(userInput)) {
                            musicListSearch.add(song)
                        }
                    }
                    search = true
                    musicAdapeter.updateMusicList(searchList = musicListSearch)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}