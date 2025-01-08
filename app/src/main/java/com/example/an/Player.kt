package com.example.an

import java.io.Serializable

//import kotlinx.serialization.Serializable
@kotlinx.serialization.Serializable
data class Player(val name:String,val score:Int):Serializable