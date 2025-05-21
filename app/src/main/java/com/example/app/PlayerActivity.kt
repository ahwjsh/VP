package com.example.app

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlayerActivity : AppCompatActivity() {

    private lateinit var bGoToMainActivity : Button

    private lateinit var prev: Button
    private lateinit var play: Button
    private lateinit var pause: Button
    private lateinit var next: Button
    private lateinit var cycle: Button
    private lateinit var volume: SeekBar
    private lateinit var currentTime: SeekBar

    private lateinit var mediaPlayer: MediaPlayer
    private var songTime = 0

    private val songs = listOf(R.raw.test1, R.raw.test2)
    private var songIndex = 0
    private var isCycling = false
    private val handler = Handler(Looper.getMainLooper())
    private var isSeekBarTracking = false

    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (!isSeekBarTracking && mediaPlayer.isPlaying) {
                currentTime.progress = mediaPlayer.currentPosition
            }
            handler.postDelayed(this, 200)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bGoToMainActivity = findViewById(R.id.go_to_main_activity)
        bGoToMainActivity.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        prev = findViewById(R.id.previous)
        play = findViewById(R.id.play)
        pause = findViewById(R.id.pause)
        next = findViewById(R.id.next)
        cycle = findViewById(R.id.cycle)
        volume = findViewById(R.id.volume)
        currentTime = findViewById(R.id.time)

        initMediaPlayer()

        prev.setOnClickListener { prevSong() }
        play.setOnClickListener { mediaPlayer.start() }
        pause.setOnClickListener { mediaPlayer.pause() }
        next.setOnClickListener { nextSong() }
        cycle.setOnClickListener { cycle() }
        volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mediaPlayer.setVolume(progress / 100f, progress / 100f)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        currentTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isSeekBarTracking = true
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isSeekBarTracking = false
            }
        })

        handler.post(updateSeekBar)
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, songs[songIndex])
        songTime = mediaPlayer.duration
        currentTime.max = songTime
    }

    private fun nextSong() {
        mediaPlayer.pause()
        songIndex++
        if (songIndex>=songs.size){
            songIndex=0
        }
        initMediaPlayer()
        mediaPlayer.start()
        currentTime.progress = 0
    }

    private fun prevSong() {
        mediaPlayer.pause()
        songIndex--
        if (songIndex<0) {
            songIndex = songs.size-1
        }
        initMediaPlayer()
        mediaPlayer.start()
        currentTime.progress = 0
    }

    private fun cycle() {
        isCycling = !isCycling
        mediaPlayer.isLooping = isCycling
        cycle.text = if (isCycling) "cycle on" else "cycle off"
    }

}