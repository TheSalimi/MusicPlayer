package com.example.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat

class MusicService : Service()  , AudioManager.OnAudioFocusChangeListener {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable : Runnable
    lateinit var audioManager: AudioManager

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(PlayPauseBTN : Int  , playbackSpeed : Float) {
        val intent = Intent(
            baseContext,
            MainActivity::class.java
        )
        val contentIntent = PendingIntent.getActivity(this , 0 , intent , 0)


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

        val imageArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        val image = if(imageArt != null){
            BitmapFactory.decodeByteArray(imageArt , 0 , imageArt.size)
        } else {
            BitmapFactory.decodeResource( resources , R.drawable.musicplayer_icon)
        }

        val notifiction = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.splash_screen)
            .setLargeIcon(image)
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            mediaSession.setMetadata(MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION , mediaPlayer!!.duration.toLong())
                .build()
            )
            mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING ,
                mediaPlayer!!.currentPosition.toLong()
                ,playbackSpeed)
                .build())
        }

        startForeground(13, notifiction)
    }

    fun creatMediaPlayer() {
        try {
            if (PlayerActivity.musicService!!.mediaPlayer == null)
                PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()

            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()
            PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause , 0F)

            PlayerActivity.binding.SeekBarStartTime.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.SeekBarEndTime.text = formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.SeekBar.progress = 0
            PlayerActivity.binding.SeekBar.max = mediaPlayer!!.duration
            PlayerActivity.nowPlayingId = PlayerActivity.musicListPA[PlayerActivity.songPosition].id
        } catch (E: java.lang.Exception) {
            return
        }
    }

    fun seekBarSetup(){
        runnable = Runnable {
            PlayerActivity.binding.SeekBarStartTime.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.SeekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper())
                .postDelayed(runnable , 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable , 0)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange <= 0){
            //pause music
            PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_play)
            NowPlaying.binding.IsPlayingPLayOrPause.setIconResource(R.drawable.ic_play)
            showNotification(R.drawable.ic_play , 0F)
            PlayerActivity.isPlaying = false
            mediaPlayer!!.pause()
        }
        else{
            //play music
            PlayerActivity.binding.playPauseButton.setIconResource(R.drawable.ic_pause)
            NowPlaying.binding.IsPlayingPLayOrPause.setIconResource(R.drawable.ic_pause)
            showNotification(R.drawable.ic_pause , 1F)
            PlayerActivity.isPlaying = true
            mediaPlayer!!.start()
        }
    }
}