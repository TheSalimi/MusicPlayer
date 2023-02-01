package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.ActivityPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
        var repeatSong: Boolean = false
        var _15min: Boolean = false
        var _30min: Boolean = false
        var _60min: Boolean = false
        var nowPlayingId: String = ""
        var isFavorite: Boolean = false
        var fIndex: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.data?.scheme.contentEquals("content")){
            startService()
            musicListPA = ArrayList()
            musicListPA.add(getMusicDetails(intent.data!!))
            Glide.with(this)
                .load(getImgArt(musicListPA[songPosition].path))
                .apply(RequestOptions().centerCrop())
                .into(binding.musicPic)
            songName.text = musicListPA[songPosition].title
        }
        else initializeLayout()

        BackToPreviousPageBtn.setOnClickListener { finish() }

        playPauseButton.setOnClickListener {
            if (isPlaying) pause()
            else playMusic()
        }

        nextBtn.setOnClickListener { preOrNextSong(true) }

        preBtn.setOnClickListener { preOrNextSong(false) }

        SeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        repeat.setOnClickListener { repeatItem() }

        equalizer.setOnClickListener { setEqualizer() }

        timer.setOnClickListener { setTimer() }

        share.setOnClickListener { shareBtn() }

        addToFavorites.setOnClickListener {
            if (isFavorite) removeFromFavorites()
            else AddtoFavorites()
        }
    }

    private fun removeFromFavorites() {
        fIndex = favoriteChecker(musicListPA[songPosition].id)

        isFavorite = false
        addToFavorites.setImageResource(
            R.drawable.ic_fav24
        )
        FavoritesActivity.favorieSongs.removeAt(fIndex)

    }

    private fun AddtoFavorites() {
        isFavorite = true
        addToFavorites.setImageResource(
            R.drawable.filled_favorite
        )
        FavoritesActivity.favorieSongs.add(musicListPA[songPosition])
    }

    private fun shareBtn() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(musicListPA[songPosition].path))
        startActivity(Intent.createChooser(shareIntent, "Share"))
    }

    private fun setTimer() {
        val timer = _15min || _30min || _60min
        if (!timer)
            showBottomSheetDialog()
        else {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Stop Timer")
                .setMessage("Do you want to stop timer?")
                .setPositiveButton("Yes") { _, _ ->
                    _15min = false
                    _30min = false
                    _60min = false
                    binding.timer.setColorFilter(ContextCompat.getColor(this, R.color.black))
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
    }

    private fun setEqualizer() {
        try {
            val EqIntent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            EqIntent.putExtra(
                AudioEffect.EXTRA_AUDIO_SESSION,
                musicService!!.mediaPlayer!!.audioSessionId
            )
            EqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, baseContext.packageName)
            EqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            startActivityForResult(EqIntent, 13)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Equalizer feature not supported", Toast.LENGTH_SHORT).show()
        }
    }

    private fun repeatItem() {
        if (!repeatSong) {
            repeatSong = true
            repeat.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        } else {
            repeatSong = false
            repeat.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun startService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "PlaylistDetailsShuffle"->{
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList)
                musicListPA.shuffle()
                setLayout()
            }
            "FavoriteShuffle" -> {
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(FavoritesActivity.favorieSongs)
                musicListPA.shuffle()
                setLayout()
            }
            "FavoriteAdapter" -> {
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(FavoritesActivity.favorieSongs)
                setLayout()
            }
            "NowPlaying" -> {
                setLayout()
                binding.SeekBarStartTime.text =
                    formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.SeekBarEndTime.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.SeekBar.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.SeekBar.max = musicService!!.mediaPlayer!!.duration
                if (isPlaying) playPauseButton.setIconResource(R.drawable.ic_pause)
                else playPauseButton.setIconResource(R.drawable.ic_play)
            }
            "MusicAdapterSearch" -> {
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicListSearch)
                setLayout()
            }
            "MusicAdapter" -> {
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicList)
                setLayout()
            }
            "MainActivity" -> {
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.musicList)
                musicListPA.shuffle()
                setLayout()
            }
            "PlayListDetailsAdapter"->{
                startService()
                musicListPA = ArrayList()
                musicListPA.addAll(playlistActivity.musicPlayList.ref[PlayListDetails.currentPlayListPosition].playList)
                setLayout()
            }
        }
        if(musicService!=null &&  !isPlaying) {
            playMusic()
        }
    }

    private fun setLayout() {
        fIndex = favoriteChecker(musicListPA[songPosition].id)
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_splash).centerCrop())
            .into(binding.musicPic)
        songName.text = musicListPA[songPosition].title
        if (repeatSong) repeat.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        if (_15min || _60min || _30min)
            timer.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        if (isFavorite) addToFavorites.setImageResource(R.drawable.filled_favorite)
        else addToFavorites.setImageResource(R.drawable.ic_fav24)
    }

    private fun creatMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()

            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            playPauseButton.setIconResource(R.drawable.ic_pause)
            musicService!!.showNotification(R.drawable.ic_pause , 1F)

            SeekBarStartTime.text =
                formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            SeekBarEndTime.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            SeekBar.progress = 0
            SeekBar.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            nowPlayingId = musicListPA[songPosition].id
            playMusic()
        } catch (E: Exception) {
            Toast.makeText(this , E.toString() , Toast.LENGTH_LONG).show()
        }
    }

    private fun playMusic() {
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
        playPauseButton.setIconResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause , 1F)
    }

    private fun pause() {
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        playPauseButton.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play , 0F)
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
        if(musicService == null) {
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()
            musicService!!.audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            musicService!!.audioManager.requestAudioFocus(musicService , AudioManager.STREAM_MUSIC , AudioManager.AUDIOFOCUS_GAIN)
        }
        creatMediaPlayer()
        musicService!!.seekBarSetup() }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setSongPosition(true)
        creatMediaPlayer()
        setLayout()

        //for refreshing now playing image and text on song completion

        NowPlaying.binding.IsPlayingName.isSelected = true
        Glide.with(applicationContext)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().centerCrop())
            .into(NowPlaying.binding.IsPlayingImage)
        NowPlaying.binding.IsPlayingName.text = musicListPA[songPosition].title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 || resultCode == RESULT_OK)
            return
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        dialog.min15.setOnClickListener {
            Toast.makeText(this, "Music will stop after 15 minutes", Toast.LENGTH_SHORT).show()
            timer.setColorFilter(ContextCompat.getColor(this, R.color.gray))
            _15min = true
            Thread {
                Thread.sleep((15 * 60000).toLong())
                if (_15min) exitApplication()
            }.start()
            dialog.dismiss()
        }

        dialog.min30.setOnClickListener {
            Toast.makeText(this, "Music will stop after 30 minutes", Toast.LENGTH_SHORT).show()
            timer.setColorFilter(ContextCompat.getColor(this, R.color.gray))
            _30min = true
            Thread {
                Thread.sleep((30 * 60000).toLong())
                if (_30min) exitApplication()
            }.start()
            dialog.dismiss()
        }

        dialog.min60.setOnClickListener {
            Toast.makeText(this, "Music will stop after 60 minutes", Toast.LENGTH_SHORT).show()
            timer.setColorFilter(ContextCompat.getColor(this, R.color.gray))
            _60min = true
            Thread {
                Thread.sleep((60 * 60000).toLong())
                if (_60min) exitApplication()
            }.start()
            dialog.dismiss()
        }
    }

    private fun getMusicDetails(contentUri:Uri) : Music{
        var cursor : Cursor?=null
        try{
            val projection = arrayOf(MediaStore.Audio.Media.DATA , MediaStore.Audio.Media.DURATION)
            cursor = this.contentResolver.query(contentUri , projection , null , null , null)
            val dataColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            cursor!!.moveToFirst()
            val path = dataColumn?.let{
                cursor.getString(it)
            }
            val duration = durationColumn?.let{cursor.getLong(it)}!!

            return Music(id = "Unknown" , title = path.toString() , album = "Unknown",
            artist = "Unknown" , duration = duration ,
            artUri = "Unknown", path = path.toString())
        }
        finally {
            cursor?.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(musicListPA[songPosition].id == "Unknown" && !isPlaying) exitApplication()
    }

}