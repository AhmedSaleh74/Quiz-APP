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
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.an.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.serialization.json.JsonObject

class MainActivity : AppCompatActivity(),View.OnClickListener{
    lateinit var binding:ActivityMainBinding
    lateinit var queue : RequestQueue
    lateinit var question : QuestionModel
    lateinit var adapter: ArrayAdapter<String>
    var score = 0
    var index = 0
    var answers = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        queue = Volley.newRequestQueue(this)
        getCategory()
        getQuestion()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, answers)
        binding.answerText.adapter = adapter
        binding.helloNameText.text = "Hello ${intent.getStringExtra("name")}"
        binding.restartButton.setOnClickListener(this)
        binding.nextButton.setOnClickListener(this)
    }
    fun updateAdapter(){
        if (answers.isNotEmpty()){
            adapter.clear()
            adapter.addAll(answers)
            adapter.notifyDataSetChanged()
        }
    }
    fun getCategory():String{
        val category = intent.getStringExtra("category").toString()
        return when (category) {
            "General Knowledge" -> "9"
            "Geography" -> "22"
            "Sports" -> "21"
            "Mythology" -> "20"
            "Entertainment: Video Games" -> "15"
            "Entertainment: Books" -> "10"
            "Art" -> "25"
            "History" -> "23"
            else -> "9"
        }
    }
    fun getQuestion(){
        val numQuestion = intent.getStringExtra("numQuestion")
        val difficulty = intent.getStringExtra("difficulty")
        val url = "https://opentdb.com/api.php?amount=$numQuestion&category=${getCategory()}&difficulty=$difficulty"
        val request = JsonObjectRequest(url,{
            val gson = Gson()
            question = gson.fromJson(it.toString(),QuestionModel::class.java)
            binding.questionText.text = question.results[index].question
            answers= question.results[index].incorrectAnswers.toMutableList()
            answers.add(question.results[index].correctAnswer)
            updateAdapter()
        },{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        })
        queue.add(request)
    }
    fun answers (){
        if (binding.answerText.selectedItem.toString() == question.results[index].correctAnswer){
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show()
            score++
        }else{
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show()
        }
    }
    fun start() {
        score=0
        index=0
        binding.nextButton.isEnabled = true
        binding.answerText.isEnabled= true
        binding.answerText.setSelection(0)
        binding.questionText.text = question.results[index].question
        answers.shuffle()
        answers = question.results[index].incorrectAnswers.toMutableList()
        answers.add(question.results[index].correctAnswer)
        updateAdapter()
    }
    fun next() {
        val answer = binding.answerText.selectedItem.toString()
        if (answer.isEmpty()){
            Toast.makeText(this, "Please Enter the answer", Toast.LENGTH_SHORT).show()
            return
        }
        answers()
        index++
        if(index < question.results.size){
            binding.questionText.text = question.results[index].question
            answers=question.results[index].incorrectAnswers.toMutableList()
            answers.add(question.results[index].correctAnswer)
            updateAdapter()
        }
        else{
            binding.nextButton.isEnabled=false
            binding.answerText.isEnabled=false
            binding.questionText.setText("")
            val intent = Intent(this,PlayerActivity::class.java)
            intent.putExtra("score",score)
            intent.putExtra("countryCitiesSize",question.results.size)
            setResult(RESULT_OK,intent)
            finish()
        }
        answers.subList(1,answers.size).shuffle()
        binding.answerText.setSelection(0)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.nextButton -> next()
            R.id.restartButton -> start()
        }
    }
}