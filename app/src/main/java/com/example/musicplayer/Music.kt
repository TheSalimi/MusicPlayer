package com.example.musicplayer

import android.net.Uri

class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri:String
) {

}
