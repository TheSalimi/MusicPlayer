package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.fragment_now_playing.*
import kotlin.system.exitProcess

class NotificatinReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS -> prevOrNextSong(false , context!!)
            ApplicationClass.PLAY -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> prevOrNextSong(true , context!!)
            ApplicationClass.EXIT -> {
                exitApplication()
            }
        }
    }

    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_pause)
        NowPlaying.binding.IsPlayingPLayOrPause.setIconResource(R.drawable.ic_pause)
    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_play)
        NowPlaying.binding.IsPlayingPLayOrPause.setIconResource(R.drawable.ic_play)
    }

    private fun prevOrNextSong(increment : Boolean , context: Context){
        setSongPosition(increment)
        PlayerActivity.musicService!!.creatMediaPlayer()
        Glide.with(context).load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .into(PlayerActivity.binding.musicPic)
        PlayerActivity.binding.songName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        //set NowPlaying layout
        Glide.with(context).load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .into(NowPlaying.binding.IsPlayingImage)
        NowPlaying.binding.IsPlayingName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        NowPlaying.binding.IsPlayingArtistName.text =
            PlayerActivity.musicListPA[PlayerActivity.songPosition].artist
        playMusic()
        PlayerActivity.fIndex = favoriteChecker(PlayerActivity.musicListPA[PlayerActivity.songPosition].id)
        if(PlayerActivity.isFavorite) PlayerActivity.binding.addToFavorites.setImageResource(R.drawable.filled_favorite)
        else PlayerActivity.binding.addToFavorites.setImageResource(R.drawable.ic_fav24)
    }


}