package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.MusicTileStyleBinding

@Suppress("DEPRECATION")
class MusicAdapter(private val context: Context, private val musicList: ArrayList<Music>): RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    class MusicViewHolder(binding: MusicTileStyleBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.musicname
        val artist = binding.artistname
        val duration = binding.duration
        val img = binding.musicicon
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(MusicTileStyleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.artist.text = musicList[position].artist
        holder.duration.text = musicList[position].duration
        Glide.with(context)
            .load(musicList[position].albumArt)
            .apply(RequestOptions().placeholder(R.drawable.music_app_icon).centerCrop())
            .into(holder.img)
        holder.root.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("index", position)
            intent.putExtra("class", "MainActivity")
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}

