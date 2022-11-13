package com.example.musicplayer

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FragmentNowPlayingBinding
import com.google.android.material.resources.CancelableFontCallback.ApplyFont
import kotlinx.android.synthetic.main.fragment_now_playing.*

class NowPlaying : Fragment() {
    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var binding: FragmentNowPlayingBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.INVISIBLE
        binding.IsPlayingPLayOrPause.setOnClickListener {
            if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
        }
        binding.IsPlayingNext.setOnClickListener { nextSong() }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.musicService != null) {
            binding.root.visibility = View.VISIBLE
            IsPlayingName.isSelected = true
            Glide.with(this).load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
                .into(IsPlayingImage)
            IsPlayingName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
            IsPlayingArtistName.text =
                PlayerActivity.musicListPA[PlayerActivity.songPosition].artist
            if (PlayerActivity.isPlaying) IsPlayingPLayOrPause.setIconResource(R.drawable.ic_pause)
            else IsPlayingPLayOrPause.setIconResource(R.drawable.ic_play)

        }
    }

    private fun playMusic() {
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        IsPlayingPLayOrPause.setIconResource(R.drawable.ic_pause)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_pause)
    }

    private fun pauseMusic() {
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        IsPlayingPLayOrPause.setIconResource(R.drawable.ic_play)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_play)
    }

    fun nextSong() {
        setSongPosition(true)
        PlayerActivity.musicService!!.creatMediaPlayer()
        Glide.with(this).load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .into(IsPlayingImage)
        IsPlayingName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        IsPlayingArtistName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].artist
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        playMusic()
    }
}