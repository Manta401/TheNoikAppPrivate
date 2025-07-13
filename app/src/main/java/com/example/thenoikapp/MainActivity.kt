package com.example.thenoikapp

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import kotlin.random.Random

class MainActivity : Activity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var currentTrackResId: Int? = null
    private val trackIds = mutableListOf<Int>()
    private var isReproduceAllEnabled = false
    private var currentTrackIndex = 0

    private lateinit var nikoImageView: ImageView  // Aggiunta per gestire l'immagine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializza l'immagine di Niko
        nikoImageView = findViewById(R.id.nikoImageView)
        nikoImageView.visibility = View.GONE

        // Carica tutti i brani beep1-beep58
        for (i in 1..58) {
            val soundId = resources.getIdentifier("beep$i", "raw", packageName)
            if (soundId != 0) {
                trackIds.add(soundId)
            }
        }

        // Inizializza i pulsanti musicali
        for (i in 1..58) {
            val buttonId = resources.getIdentifier("beep${i}Button", "id", packageName)
            val soundId = resources.getIdentifier("beep$i", "raw", packageName)

            val button = findViewById<Button>(buttonId)
            button?.setOnClickListener {
                currentTrackIndex = trackIds.indexOf(soundId)
                playSoundInBackground(soundId)
            }
        }

        // Play/Pausa
        val playPauseButton = findViewById<Button>(R.id.playPauseButton)
        playPauseButton.setOnClickListener {
            if (mediaPlayer != null) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer?.pause()
                    isPaused = true
                } else {
                    mediaPlayer?.start()
                    isPaused = false
                }
            }
        }

        // Stop
        val stopButton = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            isReproduceAllEnabled = false
            stopPlayback()
        }

        // Shuffle
        val shuffleButton = findViewById<Button>(R.id.shuffleButton)
        shuffleButton.setOnClickListener {
            val randomIndex = Random.nextInt(trackIds.size)
            currentTrackIndex = randomIndex
            playSoundInBackground(trackIds[randomIndex])
        }

        // CheckBox Reproduce All
        val reproduceAllCheckbox = findViewById<CheckBox>(R.id.checkbox_reproduce_all)
        reproduceAllCheckbox.setOnCheckedChangeListener { _, isChecked ->
            isReproduceAllEnabled = isChecked
        }

        // Check for exact alarm permission before setting any alarm (if necessary)
        checkExactAlarmPermission(this)
    }

    // Function to check exact alarm permission on Android 12+
    private fun checkExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Open system settings so user can enable the permission
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }

    private fun playSoundInBackground(soundResId: Int) {
        if (currentTrackResId == soundResId && isPaused) {
            mediaPlayer?.start()
            isPaused = false
            return
        }

        stopPlayback()

        mediaPlayer = MediaPlayer.create(this, soundResId)
        currentTrackResId = soundResId
        isPaused = false

        // Mostra immagine di Niko
        nikoImageView.setImageResource(R.drawable.niko_img_5)
        nikoImageView.visibility = View.VISIBLE

        mediaPlayer?.isLooping = !isReproduceAllEnabled

        mediaPlayer?.setOnCompletionListener {
            if (isReproduceAllEnabled) {
                playNextTrack()
            }
        }

        mediaPlayer?.start()
        moveTaskToBack(true)
    }

    private fun playNextTrack() {
        currentTrackIndex++
        if (currentTrackIndex < trackIds.size) {
            val nextResId = trackIds[currentTrackIndex]
            playSoundInBackground(nextResId)
        } else {
            isReproduceAllEnabled = false
        }
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPaused = false
        currentTrackResId = null

        // Nasconde immagine quando si clicca stop
        nikoImageView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
    }
}
