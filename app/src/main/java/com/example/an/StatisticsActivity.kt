package com.example.an

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged

class StatisticsActivity : AppCompatActivity() {
    lateinit var playersLV:ListView
    lateinit var etFilter:EditText
    lateinit var players: MutableList<Player>
    lateinit var adapter:ArrayAdapter<String>
    var sortedByScore = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playersLV = findViewById(R.id.playersLV)
        etFilter = findViewById(R.id.etFilter)
        etFilter.doAfterTextChanged { changed->
            val names = players.filter { it.name.startsWith(changed.toString()) }.map { it.name }
            adapter.clear()
            adapter.addAll(names)
        }
        players = (intent.getSerializableExtra("players") as Array<Player>).toMutableList()
        val names = players.map { it.name }
        adapter = ArrayAdapter(this,R.layout.custom_player_layout,R.id.cplPlayerName,names)
        playersLV.adapter = adapter
    }

    fun sortByScore(view: View) {
        if (!sortedByScore){
            val names = players.sortedByDescending { it.score }.map { it.name }
            adapter.clear()
            adapter.addAll(names)
        }else{
            val names = players.sortedBy { it.score }.map { it.name }
            adapter.clear()
            adapter.addAll(names)
        }
        sortedByScore = !sortedByScore
    }
}