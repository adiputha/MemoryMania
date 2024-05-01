package com.example.memorygame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var mediaPlayer: MediaPlayer? = null // Nullable MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic)

        sharedPreferences = getSharedPreferences("game_settings", Context.MODE_PRIVATE)

        val soundEnabled = loadSettings()

        binding.switchSound.isChecked = soundEnabled

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startBackgroundMusic()
            } else {
                stopBackgroundMusic()
            }
            saveSettings(isChecked)
        }

        binding.buttonSave.setOnClickListener {
            val isChecked = binding.switchSound.isChecked
            saveSettings(isChecked)
            startActivity(Intent(this@SettingsActivity, StartActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed
        mediaPlayer?.release()
    }

    private fun saveSettings(soundEnabled: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("sound_enabled", soundEnabled)
        editor.apply()
    }

    private fun loadSettings(): Boolean {
        return sharedPreferences.getBoolean("sound_enabled", true)
    }

    private fun startBackgroundMusic() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.isLooping = true
                player.start()
            }
        }
    }

    private fun stopBackgroundMusic() {
        mediaPlayer?.pause()
    }
}
