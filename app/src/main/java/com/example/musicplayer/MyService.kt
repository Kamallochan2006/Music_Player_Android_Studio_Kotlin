package com.example.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.musicplayer.MainActivity.Companion.musicList
import com.example.musicplayer.PlayerActivity.Companion.binding
import com.example.musicplayer.PlayerActivity.Companion.isPlaying
import com.example.musicplayer.PlayerActivity.Companion.musicService
import com.example.musicplayer.PlayerActivity.Companion.songPosition

class MyService : Service() {
    private val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    fun createMediaPlayer() {
        if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(musicList[songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        isPlaying = true
        binding.playPause.setIconResource(R.drawable.pause_circle)
        binding.seekBarTime.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
        binding.musicDuration.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
        binding.seekBar.progress = 0
        binding.seekBar.max = musicService!!.mediaPlayer!!.duration
    }

    fun seekBarProgress(){
        runnable = Runnable {
            binding.seekBarTime.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.seekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}