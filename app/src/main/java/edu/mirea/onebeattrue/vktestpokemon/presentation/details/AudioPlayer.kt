package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.IOException

class AudioPlayer {
    private val mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    fun startPlaying(audioUrl: String) {
        try {
            mediaPlayer.apply {
                reset()
                setDataSource(audioUrl)
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}