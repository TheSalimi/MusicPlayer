package com.example.musicplayer

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.content.ContextCompat
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

data class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
)

class PlayList{
    lateinit var  name : String
    lateinit var  playList : ArrayList<Music>
    lateinit var createdOn:String
}

class  MusicPlayList{
    var ref : ArrayList<PlayList> = ArrayList()
}

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)
}

fun getImgArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun setSongPosition(increment: Boolean) {
    if (!PlayerActivity.repeatSong) {
        if (increment) {
            if (PlayerActivity.musicListPA.size - 1 == PlayerActivity.songPosition)
                PlayerActivity.songPosition = 0
            else
                ++PlayerActivity.songPosition
        } else {
            if (PlayerActivity.songPosition == 0) {
                PlayerActivity.songPosition = PlayerActivity.musicListPA.size - 1
            } else
                --PlayerActivity.songPosition
        }
    }
}


fun exitApplication() {
    if (PlayerActivity.musicService != null) {
        PlayerActivity.musicService!!.stopForeground(true)
        PlayerActivity.musicService!!.mediaPlayer!!.release()
        PlayerActivity.musicService = null
    }
    exitProcess(1)
}

fun favoriteChecker(id: String): Int {
    PlayerActivity.isFavorite = false
    FavoritesActivity.favorieSongs.forEachIndexed { index, music ->
        if (id == music.id) {
            PlayerActivity.isFavorite = true
            return index
        }
    }
    return -1
}

fun checkPlayList(playList: ArrayList<Music>): ArrayList<Music>{
    playList.forEachIndexed{ index, music ->
        val file = File(music.path)
        if(!file.exists()){
            playList.removeAt(index)
        }
    }
    return playList
}



