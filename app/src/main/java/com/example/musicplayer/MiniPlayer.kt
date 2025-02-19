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
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.MainActivity.Companion.musicList
import com.example.musicplayer.PlayerActivity.Companion.songPosition
import com.example.musicplayer.databinding.FragmentMiniPlayerBinding

@Suppress("DEPRECATION")
class MiniPlayer : Fragment() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentMiniPlayerBinding
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mini_player, container, false)
        binding = FragmentMiniPlayerBinding.bind(view)
        binding.root.visibility = View.GONE
        binding.miniPlayPause.setOnClickListener {
            if (PlayerActivity.isPlaying){
                pauseMusic()
            }else{
                playMusic()
            }
        }
        binding.miniNextMusic.setOnClickListener {
            nextMusic()
        }
        binding.miniPreviousMusic.setOnClickListener {
            prevMusic()
        }
        binding.root.setOnClickListener {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("index", songPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(), intent, null)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.musicService != null) {
            setLayout()
        }
    }

    private fun playMusic() {
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.miniPlayPause.setImageResource(R.drawable.pause_circle)
        PlayerActivity.binding.playPause.setIconResource(R.drawable.pause_circle)
        PlayerActivity.isPlaying = true
    }
    private fun pauseMusic() {
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.miniPlayPause.setImageResource(R.drawable.play_circle)
        PlayerActivity.binding.playPause.setIconResource(R.drawable.play_circle)
        PlayerActivity.isPlaying = false
    }
    private fun nextMusic() {
        setSongPosition(increment = true)
        setImgTitle()
    }
    private fun prevMusic() {
        setSongPosition(increment = false)
        setImgTitle()
    }
    private fun setImgTitle(){
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(this)
            .load(musicList[songPosition].albumArt)
            .apply(RequestOptions().placeholder(R.drawable.music_app_icon).centerCrop())
            .into(binding.miniMusicIcon)
        binding.miniSongName.text = musicList[songPosition].title
        binding.miniArtistName.text = musicList[songPosition].artist
        playMusic()
    }

    private fun setLayout(){
        binding.root.visibility = View.VISIBLE
        binding.miniSongName.isSelected = true
        Glide.with(this)
            .load(musicList[songPosition].albumArt)
            .apply(RequestOptions().placeholder(R.drawable.music_app_icon).centerCrop())
            .into(binding.miniMusicIcon)
        binding.miniSongName.text = musicList[songPosition].title
        binding.miniArtistName.text = musicList[songPosition].artist
        if (PlayerActivity.isPlaying) binding.miniPlayPause.setImageResource(R.drawable.pause_circle)
        else binding.miniPlayPause.setImageResource(R.drawable.play_circle)
    }
}