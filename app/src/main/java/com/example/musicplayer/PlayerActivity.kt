package com.example.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore.Audio.Media
import android.text.BoringLayout
import androidx.core.app.ServiceCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() ,ServiceConnection {

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService : MusicService?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        startService()
        initializeLayout()

        playPauseButton.setOnClickListener {
            if (isPlaying) pause()
            else playMusic()
        }

        nextBtn.setOnClickListener {
            preOrNextSong(true)
        }

        preBtn.setOnClickListener {
            preOrNextSong(false)
        }
    }

    private fun startService(){
        val intent = Intent(this , MusicService::class.java)
        bindService(intent , this , BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicList)
                setLayout()
            }
            "MainActivity"->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicList)
                musicListPA.shuffle()
                setLayout()
            }
        }
    }

    private fun setLayout() {
        Glide.with(this).load(musicListPA[songPosition].artUri)
            .into(musicPic)
        songName.text = musicListPA[songPosition].title.toString()
    }

    private fun creatMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null)
                musicService!!.mediaPlayer = MediaPlayer()

            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            playPauseButton.setIconResource(R.drawable.ic_pause)

        } catch (E: java.lang.Exception) {
            return
        }
    }

    private fun playMusic() {
        playPauseButton.setIconResource(R.drawable.ic_pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pause() {
        playPauseButton.setIconResource(R.drawable.ic_play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun preOrNextSong(increment: Boolean) {
        if (increment) {
            setSongPosition(increment)
            setLayout()
            creatMediaPlayer()
        } else {
            setSongPosition(increment)
            setLayout()
            creatMediaPlayer()
        }
    }

    private fun setSongPosition(increment: Boolean) {
        if (increment) {
            if (musicListPA.size - 1 == songPosition)
                songPosition = 0
            else
                ++songPosition
        } else {
            if (songPosition == 0) {
                songPosition = musicListPA.size - 1
            } else
                --songPosition
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        creatMediaPlayer()
        musicService!!.showNotification()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}