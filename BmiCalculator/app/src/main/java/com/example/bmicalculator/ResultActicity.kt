package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.toast

class ResultActicity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val height = intent?.getStringExtra("height")?.toInt()?:0   //인텐트에 각각 height,weight라는 이름으로 담겼던 값을 저장한다.
        val weight = intent?.getStringExtra("weight")?.toInt()?:0

        val bmi = weight?.div(Math.pow(height / 100.0, 2.0))

        when {
            bmi >=35 -> resultTextView.text = "고도 비만"
            bmi >=30 -> resultTextView.text = "2단계 비만"
            bmi >=25 -> resultTextView.text = "1단계 비만"
            bmi >=23 -> resultTextView.text = "과제중"
            bmi >=18.5 -> resultTextView.text = "정상"
            else -> resultTextView.text = "저체중"
        }

        when {
            bmi >= 23 ->
                imageView.setImageResource(
                    R.drawable.high_face
                )
            bmi >= 18.5 ->
                imageView.setImageResource(
                    R.drawable.normal_face
                )
            else ->
                imageView.setImageResource(
                    R.drawable.low_face
                )
        }
        toast("$bmi")
    }
}