package com.example.an

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    lateinit var questionText:TextView
    lateinit var helloNameTxt : TextView
    lateinit var answerText:Spinner
    lateinit var nextButton: Button
    var score = 0
    var index = 0
//    val countries = arrayOf("egypt","usa","france","uk")
//    val cities = arrayOf("cairo","ws","paris","london")
    val countryCapitals = mutableListOf(
        Question("egypt","cairo"),
        Question("usa","ws"),
        Question("france","paris"),
        Question("uk","london"),
    )
    val originalItems = mutableListOf(
        "please selected", "cairo", "kuwait", "ws","london",
        "istanbul", "baghdad", "damascus", "paris", "makkah",
        )
    var availableItems = originalItems.toList()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        questionText = findViewById(R.id.questionText)
        helloNameTxt = findViewById(R.id.helloNameText)
        answerText = findViewById(R.id.answerText)
        nextButton = findViewById(R.id.nextButton)
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, originalItems)
        answerText.adapter = countryAdapter
        countryCapitals.shuffle()
        helloNameTxt.text = "Hello ${intent.getStringExtra("name")}"
        questionText.text = "What is the capital of ${countryCapitals[index].country}"
    }


    fun start(view: View) {
        score=0
        index=0
        originalItems.clear()
        originalItems.addAll(availableItems)
        nextButton.isEnabled = true
        answerText.isEnabled= true
        countryCapitals.shuffle()
        answerText.setSelection(0)
        questionText.text = "What is the capital of ${countryCapitals[index].country}"
    }
    fun next(view: View) {
        val answer = answerText.selectedItem.toString()
        if (answer.isEmpty() || answerText.selectedItemPosition==0){
            Toast.makeText(this, "Please Enter the City", Toast.LENGTH_SHORT).show()
            return
        }
        if(answer == countryCapitals[index].cities) {
            score++
            originalItems.remove(answer)
        }
        index++
        if(index < countryCapitals.size){
            questionText.text = "What is the capital of ${countryCapitals[index].country}"
        }
        else{
            nextButton.isEnabled=false
            answerText.isEnabled=false
            questionText.setText("")
            val intent = Intent(this,PlayerActivity::class.java)
            intent.putExtra("score",score)
            intent.putExtra("countryCitiesSize",countryCapitals.size)
            setResult(RESULT_OK,intent)
            finish()
        }
        originalItems.subList(1,originalItems.size).shuffle()
        answerText.setSelection(0)
    }
}