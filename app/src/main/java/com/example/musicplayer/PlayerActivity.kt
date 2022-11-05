package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore.Audio.Media
import android.text.BoringLayout
import android.widget.SeekBar
import android.widget.SeekBar.*
import androidx.core.app.ServiceCompat
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.ActivityPlayerBinding
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(), ServiceConnection {

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
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

        SeekBar.setOnSeekBarChangeListener(object  : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) =Unit
        })
    }

    private fun startService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
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
            "MainActivity" -> {
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

    fun creatMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null)
                musicService!!.mediaPlayer = MediaPlayer()

            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            playPauseButton.setIconResource(R.drawable.ic_pause)
            musicService!!.showNotification(R.drawable.ic_pause)

            SeekBarStartTime.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            SeekBarEndTime.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            SeekBar.progress = 0
            SeekBar.max = musicService!!.mediaPlayer!!.duration
        } catch (E: java.lang.Exception) {
            return
        }
    }

    private fun playMusic() {
        playPauseButton.setIconResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pause() {
        playPauseButton.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
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

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        creatMediaPlayer()
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}