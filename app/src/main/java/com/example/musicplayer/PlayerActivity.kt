package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.text.BoringLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
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

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicList)
                setLayout()
                creatMediaPlayer()
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
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer()

            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            playPauseButton.setIconResource(R.drawable.ic_pause)

        } catch (E: java.lang.Exception) {
            return
        }
    }

    private fun playMusic() {
        playPauseButton.setIconResource(R.drawable.ic_pause)
        isPlaying = true
        mediaPlayer!!.start()
    }

    private fun pause() {
        playPauseButton.setIconResource(R.drawable.ic_play)
        isPlaying = false
        mediaPlayer!!.pause()
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
}