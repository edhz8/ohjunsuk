package com.example.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        resultButton.setOnClickListener{
            saveData(heightEditText.text.toString().toInt(),
                weightEditText.text.toString().toInt())
            startActivity<ResultActicity>(  //인텐트에 각각 weight,height라는 이름으로 값을 담아준다.
                "weight" to weightEditText.text.toString(),
                "height" to heightEditText.text.toString()
            )
        }
    }
    private fun saveData(height: Int , weight: Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        editor.putInt("KEY_HEIGHT",height)
            .putInt("KEY_WEIGHT",weight)
            .apply()
    }
    private fun loadData(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val height = pref.getInt("KEY_HEIGHT",0)    //저장된 값이 없을때 0을 리턴한다는 뜻
        val weight = pref.getInt("KEY_WEIGHT",0)

        if(height != 0 && weight != 0){
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}