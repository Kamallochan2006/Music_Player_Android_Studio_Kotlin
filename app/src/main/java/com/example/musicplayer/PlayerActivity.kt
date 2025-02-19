package com.example.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    companion object{
        lateinit var musicList : ArrayList<Music>
        var songPosition: Int = 0
        lateinit var binding: ActivityPlayerBinding
        var isPlaying: Boolean = false
        var musicService: MyService? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMediaPlayer()
        initializeLayout()
        binding.backBtn.setOnClickListener{
            finish()
        }
        binding.playPause.setOnClickListener {
            if (isPlaying){
                pauseMusic()
            }
            else{
                playMusic()
            }
        }

        binding.nextMusic.setOnClickListener {
            nextMusic()
        }
        binding.previousMusic.setOnClickListener {
            prevMusic()
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "NowPlaying" -> {
                setLayout()
                binding.seekBarTime.text =
                    formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.musicDuration.text =
                    formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.seekBar.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.seekBar.max = musicService!!.mediaPlayer!!.duration
                if (isPlaying) binding.playPause.setIconResource(R.drawable.pause_circle)
                else binding.playPause.setIconResource(R.drawable.play_circle)
            }
            "MainActivity" -> {

                val intent = Intent(this, MyService::class.java)
                bindService(intent, this, BIND_AUTO_CREATE)
                startService(intent)
            }
        }
    }

    private fun playMusic(){
        setLayout()
        binding.playPause.setIconResource(R.drawable.pause_circle)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        binding.playPause.setIconResource(R.drawable.play_circle)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun nextMusic(){
        setLayout()
        setSongPosition(increment = true)
        createMediaPlayer()
    }

    private fun prevMusic(){
        setLayout()
        setSongPosition(increment = false)
        createMediaPlayer()
    }

    private fun createMediaPlayer(){
        setLayout()
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
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
    }

    private fun setLayout(){
        binding.songName.isSelected = true
        Glide.with(this)
            .load(musicList[songPosition].albumArt)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(binding.songIcon)
        binding.songName.text = musicList[songPosition].title
        binding.songArtist.text = musicList[songPosition].artist
    }

    private fun initMediaPlayer(){
        songPosition = intent.getIntExtra("index", 0)
        musicList = ArrayList()
        musicList.addAll(MainActivity.musicList)
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MyService.MyBinder
        musicService = binder.getService()
        createMediaPlayer()
        musicService!!.seekBarProgress()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(p0: MediaPlayer?) {
        setSongPosition(increment = true)
        createMediaPlayer()
    }
}