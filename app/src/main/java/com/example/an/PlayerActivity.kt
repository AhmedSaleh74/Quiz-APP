package com.example.an

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener

class PlayerActivity : AppCompatActivity() {
    lateinit var playerNameText: EditText
    lateinit var playButton: Button
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
                players.add(Player(playerNameText.text.toString().lowercase().trim(), score))
                playerComplete++
                if (score >= countrySize / 2) {
                    playerAudio = MediaPlayer.create(this, R.raw.success)
                    playerAudio?.start()
                } else {
                    playerAudio = MediaPlayer.create(this, R.raw.fail)
                    playerAudio?.start()
                }
            }
        } else {
            val playerName = playerNameText.text.toString().lowercase().trim()
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
        setContentView(R.layout.activity_player)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playerNameText = findViewById(R.id.playerNameText)
        playButton = findViewById(R.id.playButton)

        playerNameText.addTextChangedListener {
            playButton.isEnabled = playerNameText.text.isNotEmpty()
        }

    }

    override fun onDestroy() {
        playerAudio?.stop()
        super.onDestroy()
    }

    fun play(view: View) {
        playerAudio?.stop()
        val playerName = playerNameText.text.toString().lowercase().trim()
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

//        var playersAfterFreez = 0
//        val freezPlayerIndex = players.indexOfFirst { it.name == playerName }
//        for (i in freezPlayerIndex+1 until  players.size) {
//            if (players[i].score > -1) {
//                playersAfterFreez++
//            }
//        }
//        for (player in players){
//            if (playerName in freez) {
//                if (playersAfterFreez >= 2){
//                    freez.remove(playerName)
//                    players.remove(player)
//                    players.add(player)
//                    Toast.makeText(this, "You can play now", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this, "$playerName is frozen until 2 players finish playing", Toast.LENGTH_LONG).show()
//                    return
//                }
//            }
//        }

        val a = Intent(this, MainActivity::class.java)
        a.putExtra("name", playerName)
        launcher.launch(a)   //send & receive
//        startActivity(a)          send
    }
}