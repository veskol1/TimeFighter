 package com.raywenderlich.timefighter.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

 class MainActivity : AppCompatActivity() {

    internal lateinit var pressMeButton :Button
    internal lateinit var startGameButtun :Button
    internal lateinit var myScoreTextView :TextView
    internal lateinit var timeLeftTextView :TextView
    internal var score = 0
    internal lateinit var countDownTimer: CountDownTimer
    internal var countDownLength: Long = 20000  //20 second countDown
    internal var countDownInterval: Long = 1000 //1 second decrease
    internal var timeLeftOnTimer: Long = 6000
    internal var gameStarted = false
    internal val TAG = MainActivity::class.java.simpleName

     companion object {
         private const val SCORE_KEY="SCORE_KEY"
         private const val TIME_LEFT_KEY="TIME_LEFT_KEY"
         private const val GAME_STATUS_KEY="GAME_STATUS_KEY"
     }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pressMeButton = findViewById<Button>(R.id.press_me_button)
        myScoreTextView = findViewById<TextView>(R.id.score_text_View)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_View)
        startGameButtun = findViewById<Button>(R.id.start_game_button)


        if(savedInstanceState != null) {
            score= savedInstanceState.getInt(SCORE_KEY);
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            gameStarted = savedInstanceState.getBoolean(GAME_STATUS_KEY)
            initializeGame(timeLeftOnTimer)
        }
         else
            initializeGame(countDownLength)

         startGameButtun.setOnClickListener { view ->
             if(!gameStarted)
                startGame(countDownLength)
         }
         pressMeButton.setOnClickListener { view ->
             increaseScore()
        }
    }


     private fun startGame(initialTime: Long){
         gameStarted = true
         initializeGame(initialTime)
         countDownTimer.start()
     }


     private fun endGame(){
         Toast.makeText(this,getString(R.string.game_over),Toast.LENGTH_LONG).show()
         countDownTimer.cancel()
         gameStarted = false
         score=0
         countDownLength=20000
     }


     private fun initializeGame(timeLeft : Long){
         myScoreTextView.setText("your score ${score.toString()}")
         var timeLeftOnTimerBase10 = timeLeft/1000
         timeLeftTextView.setText("Time Left: ${timeLeftOnTimerBase10.toString()}")

         countDownTimer = object : CountDownTimer(timeLeft,countDownInterval){
             override fun onTick(millisUntilFinished: Long) {
                 timeLeftOnTimer=millisUntilFinished  //updated for onsaveInstance
                 timeLeftOnTimerBase10= millisUntilFinished /1000
                 timeLeftTextView.setText("Time Left: ${timeLeftOnTimerBase10.toString()}")
             }
             override fun onFinish() {
                 endGame()
             }
         }
         if(gameStarted)
            countDownTimer.start()
     }


     private fun increaseScore(){
         score++
         val newScore:String = "${getString(R.string.your_score)} ${score.toString()}"
         myScoreTextView.setText(newScore)
     }


     override fun onSaveInstanceState(outState: Bundle) {
         outState.putInt(SCORE_KEY,score)
         outState.putLong(TIME_LEFT_KEY,timeLeftOnTimer)
         outState.putBoolean(GAME_STATUS_KEY, gameStarted)
         countDownTimer.cancel()
         super.onSaveInstanceState(outState)
     }
 }