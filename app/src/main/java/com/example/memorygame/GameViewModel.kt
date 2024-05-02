package com.example.memorygame

import android.content.Context
import android.content.SharedPreferences
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorygame.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _highScore = MutableLiveData(0)
    val highScore: LiveData<Int> = _highScore

    private val _result = MutableLiveData("")
    val result: LiveData<String> = _result

    private val _userAnswer = MutableLiveData("")
    val userAnswer: LiveData<String> = _userAnswer

    private lateinit var sharedPreferences: SharedPreferences

    fun initializeSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        _highScore.value = getHighScore()
    }

    fun startGame(targetViews: List<Button>) {

        viewModelScope.launch {
            val round = (3..5).random()
            repeat(round) {
                delay(400)
                val randomPanel = (1..4).random()
                _result.value += randomPanel
                val panel = targetViews[randomPanel - 1]
                flashPanel(panel)
                delay(1000)
                resetPanel(panel)
            }
            enableButtons(targetViews)
        }
    }

    fun handleUserInput(panelId: Int, targetViews: List<Button>) {
        val currentUserAnswer = userAnswer.value ?: ""
        _userAnswer.value = currentUserAnswer + panelId.toString()
        if (_userAnswer.value == _result.value) {
            incrementScore()
            startGame(targetViews)
        } else if ((_userAnswer.value?.length ?: 0) >= (_result.value?.length ?: 0)) {
            viewModelScope.launch {
                loseAnimation(targetViews)
            }
        }
    }

    private fun incrementScore(){
        _score.value = (_score.value ?: 0) + 1
        updateScore()
    }

    private fun resetGameState() {
        _result.value = ""
        _userAnswer.value = ""
        _score.value = 0
    }

    private fun updateScore() {
        val currentScore = (_score.value ?: 0) + 1  // Increment score
        _score.value = currentScore
        if (currentScore > (_highScore.value ?: 0)) {
            _highScore.value = currentScore
            saveHighScore(currentScore)
        }
    }



    private fun loseAnimation(targetViews: List<Button>) {

        viewModelScope.launch {
            _score.value = 0
            targetViews.forEach { view ->
                view.background = AppCompatResources.getDrawable(view.context, R.drawable.btn_lose)
                delay(300)
                view.background = AppCompatResources.getDrawable(view.context, R.drawable.btn_state)
            }
            delay(1000)
            resetGameState()
            startGame(targetViews)
        }
    }

    private fun saveHighScore(highScore: Int) {
        sharedPreferences.edit().putInt("highScore", highScore).apply()
    }

    private fun getHighScore(): Int {
        return sharedPreferences.getInt("highScore", 0)
    }

    private suspend fun flashPanel(panel: Button) {
        panel.background = AppCompatResources.getDrawable(panel.context, R.drawable.btn_yellow)
    }

    private suspend fun resetPanel(panel: Button) {
        panel.background = AppCompatResources.getDrawable(panel.context, R.drawable.btn_state)
    }

    private fun enableButtons(targetViews: List<Button>) {
        targetViews.forEach { view ->
            view.isEnabled = true
        }
    }
}
