package com.example.musicplayer

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.musicplayer.ApplicationClass.Companion.CHANNEL_ID

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(PlayPauseBTN : Int) {

        val prevIntent = Intent(
            baseContext,
            NotificatinReciever::class.java
        ).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playIntent = Intent(
            baseContext,
            NotificatinReciever::class.java
        ).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextIntent = Intent(
            baseContext,
            NotificatinReciever::class.java
        ).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val exitIntent = Intent(
            baseContext,
            NotificatinReciever::class.java
        ).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notifiction = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.splash_screen)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.splash_screen))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_previous, "Previous", prevPendingIntent)
            .addAction(PlayPauseBTN, "Play", playPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_baseline_exit_to_app_24, "Exit", exitPendingIntent)
            .build()

        startForeground(13, notifiction)
    }
}