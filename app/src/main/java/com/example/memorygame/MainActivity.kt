package com.example.memorygame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.example.memorygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.initializeSharedPreferences(this)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
        }

        binding.apply {
            panel1.setOnClickListener(this@MainActivity)
            panel2.setOnClickListener(this@MainActivity)
            panel3.setOnClickListener(this@MainActivity)
            panel4.setOnClickListener(this@MainActivity)
        }

        observeViewModel()
        gameViewModel.startGame(getPanelButtons())
    }

    private fun observeViewModel() {
        gameViewModel.score.observe(this) { score ->
            binding.playerScore.text = score.toString()
        }

        gameViewModel.highScore.observe(this) { highScore ->
            binding.highScoreTextView.text = "High Score: $highScore"
        }
    }

    private fun getPanelButtons(): List<Button> {
        return listOf(binding.panel1, binding.panel2, binding.panel3, binding.panel4)
    }

    override fun onClick(view: View?) {
        view?.let { panel ->
            val panelId = when (panel.id) {
                R.id.panel1 -> 1
                R.id.panel2 -> 2
                R.id.panel3 -> 3
                R.id.panel4 -> 4
                else -> return
            }
            gameViewModel.handleUserInput(panelId, getPanelButtons())
        }
    }
}

