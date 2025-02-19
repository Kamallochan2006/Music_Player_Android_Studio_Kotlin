package com.example.musicplayer

import com.example.musicplayer.MainActivity.Companion.musicList
import com.example.musicplayer.PlayerActivity.Companion.songPosition

data class Music(val id: String, val title: String, val artist: String, val duration: String, val path: String, val albumArt: String)

fun formatDuration(durationMs: Long): String {
    val hours = (durationMs / 3600000).toInt()
    val minutes = ((durationMs % 3600000) / 60000).toInt()
    val seconds = ((durationMs % 60000) / 1000).toInt()

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds) // HH:MM:SS
    } else {
        String.format("%02d:%02d", minutes, seconds) // MM:SS
    }
}

fun setSongPosition(increment: Boolean){
    if (increment) {
        if (songPosition == musicList.size - 1) {
            songPosition = 0
        }
        else ++songPosition
    }
    else {
        if (songPosition == 0) {
            songPosition = musicList.size - 1
        }
        else --songPosition
    }
}