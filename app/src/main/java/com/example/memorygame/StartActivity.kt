package com.example.memorygame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memorygame.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val highScore = intent.getIntExtra("highScore", 0)



        binding.buttonStart.setOnClickListener{
            startActivity(Intent(this@StartActivity,MainActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener{
            startActivity(Intent(this,SettingsActivity::class.java))
        }
    }




}