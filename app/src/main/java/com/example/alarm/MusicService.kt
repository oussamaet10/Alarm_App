package com.example.alarm

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Initialize MediaPlayer with the music file
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound) // Replace with your music file
        mediaPlayer.start()

        // Release the MediaPlayer when done
        mediaPlayer.setOnCompletionListener {
            stopSelf() // Stop the service when the music is done
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer if it's still playing
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}