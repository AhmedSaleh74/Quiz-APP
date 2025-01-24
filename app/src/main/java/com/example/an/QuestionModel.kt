package com.example.an


import com.google.gson.annotations.SerializedName

data class QuestionModel(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Result>
)