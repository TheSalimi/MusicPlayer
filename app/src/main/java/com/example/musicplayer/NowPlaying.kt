package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FragmentNowPlayingBinding
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
        requireContext().theme.applyStyle(MainActivity.currentTheme[MainActivity.themeIndex] , true)
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.INVISIBLE

        binding.IsPlayingPLayOrPause.setOnClickListener {
            if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
        }

        binding.IsPlayingNext.setOnClickListener { nextSong() }
        binding.root.setOnClickListener {  Clicked() }

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

    private fun Clicked() {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("index", PlayerActivity.songPosition)
        intent.putExtra("class", "NowPlaying")
        ContextCompat.startActivity(requireContext(), intent, null)
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