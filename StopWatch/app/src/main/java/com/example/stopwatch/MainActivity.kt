package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0    //시간계산하는 변수를 0으로 초기화
    private var isRunning = false
    private var timerTask: Timer? = null    //timerTask 변수를 null을 허용하는 Timer 타입으로 선언.
    private var lap = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lapButton.setOnClickListener{
            recordLapTime()
        }

        floatingActionButton.setOnClickListener{
            isRunning = !isRunning

            if (isRunning){
                start()
            } else{
                pause()
            }
        }

        resetFab.setOnClickListener{
            reset()
        }
    }
    private fun start(){    //시작버튼(floatingActionButton)을 누를때 실행되는 함수
        floatingActionButton.setImageResource(R.drawable.pause)

        timerTask = timer(period = 10){//timer는 일정한 시간을 주기로 반복하는 동작을 수행할 때 쓰는 기능이다.
            // time을 0.01초마다 증가시키면서 UI를 갱신하고 있다.
            time++
            val sec = time / 100
            val milli = time %100
            runOnUiThread{      //timer는 워커쓰레드에서 동작하는 코드여서 UI를 변경하려면 runOnUiThread를 사용해야된다.
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }

    private fun pause(){
        floatingActionButton.setImageResource(R.drawable.play_arrow)
        timerTask?.cancel()
    }

    private fun recordLapTime(){
        val lapTime = this.time
        val textView = TextView(this)
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"
        textView.setTextSize(50.0F)
        lapLayout.addView(textView,0)
        lap ++
    }

    private fun reset(){
        timerTask?.cancel()

        time = 0
        isRunning = false
        floatingActionButton.setImageResource(R.drawable.play_arrow)
        secTextView.text = "0"
        milliTextView.text = "00"

        scrollView.removeAllViews()
        lap = 1
    }
}