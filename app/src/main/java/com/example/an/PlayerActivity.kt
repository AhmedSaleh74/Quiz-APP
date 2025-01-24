package com.example.an

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.an.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var binding:ActivityPlayerBinding
    lateinit var file : SharedPreferences
    lateinit var adapter : ArrayAdapter<String>
    val category = listOf(
        "Please Selected",
        "General Knowledge",
        "Geography",
        "Sports",
        "Mythology",
        "Entertainment: Video Games",
        "Entertainment: Books",
        "Art",
        "History"
    )
    val numQuestion = listOf("Please Selected",5,10,15,20,25,30,35,40,45,50)
    val difficulty = listOf("Please Selected","easy","hard","medium")
    val players = mutableListOf<Player>()
    val freez = mutableListOf<String>()
    var playerAudio: MediaPlayer? = null
    var outGame = 0
    var playerComplete = 0
    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            outGame = 0
            val score = it.data?.getIntExtra("score", -1)
            val countrySize = it.data?.getIntExtra("countryCitiesSize", 0)
            Toast.makeText(this, "Game finished \nScore:$score", Toast.LENGTH_SHORT).show()
            if (score != null && countrySize != null) {
                players.add(Player(binding.playerNameText.text.toString().lowercase().trim(), score))
                playerComplete++
                if (score >= countrySize / 2) {
                    playerAudio = MediaPlayer.create(this, R.raw.success)
                    playerAudio?.start()
                } else {
                    playerAudio = MediaPlayer.create(this, R.raw.fail)
                    playerAudio?.start()
                }
            }
            if (players.isNotEmpty()){
                binding.statsButton.isEnabled = true
            }
        }
        else {
            val playerName = binding.playerNameText.text.toString().lowercase().trim()
            outGame++
            if (outGame > 2){
                freez.add(playerName)
                outGame = 0
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.playerNameText.addTextChangedListener {
            binding.playButton.isEnabled = binding.playerNameText.text.isNotEmpty()
        }

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,category)
        binding.difficultySpinner.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,difficulty)
        binding.numQuestionSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,numQuestion)
        binding.categorySpinner.adapter = adapter
        file = getSharedPreferences("playerWin", MODE_PRIVATE)
        val playerString = file.getString("playerWin","")
        binding.playButton.isEnabled = file.getBoolean("playButton",false)
        if (!playerString.isNullOrEmpty()){
            val winners = playerString.split(",").filter { it.isNotEmpty() }.joinToString(", ")
            binding.finalPlayerWin.text = "Winners : $winners"
        }
        binding.playButton.setOnClickListener(this)
        binding.statsButton.setOnClickListener(this)
    }

    override fun onDestroy() {
        playerAudio?.stop()
        if (players.isNotEmpty()){
            val currentWinner = players.maxBy {it.score}
            val playerString = file.getString("playerWin", "")
            var playerWinners:MutableList<String> = mutableListOf()
            if (playerString != null){
                playerWinners = playerString.split(",").filter{it.isNotEmpty()}.toMutableList()
            }
            playerWinners.add(currentWinner.name)
            val updatedString = playerWinners.joinToString(",")
            file.edit {
                putString("playerWin",updatedString)
                putBoolean("playButton",false)
            }
        }
        super.onDestroy()
    }

    fun play() {
        playerAudio?.stop()
        val playerName = binding.playerNameText.text.toString().lowercase().trim()
        if (players.any { it.name == playerName}) {
            val player = players.first { it.name == playerName }
            Toast.makeText(this, "$playerName Played before with score${player.score}", Toast.LENGTH_SHORT).show()
            return
        }

        if (playerName in freez) {
            if (playerComplete < 2){
                Toast.makeText(this, "$playerName is frozen until 2 players finish playing", Toast.LENGTH_LONG).show()
                return
            }else{
                freez.remove(playerName)
                playerComplete = 0
                Toast.makeText(this, "$playerName can play now", Toast.LENGTH_SHORT).show()
            }
        }

        val a = Intent(this, MainActivity::class.java)
        a.putExtra("name", playerName)
        a.putExtra("category",binding.categorySpinner.selectedItem.toString())
        a.putExtra("difficulty",binding.difficultySpinner.selectedItem.toString())
        a.putExtra("numQuestion",binding.numQuestionSpinner.selectedItem.toString())
        launcher.launch(a)   //send & receive
//        startActivity(a)          send
    }

    fun statistics() {
        playerAudio?.stop()
        val a = Intent(this,StatisticsActivity::class.java)
        a.putExtra("players",players.toTypedArray())
        startActivity(a)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.statsButton -> statistics()
            R.id.playButton -> play()
        }
    }
}