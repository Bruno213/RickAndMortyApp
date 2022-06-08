package com.example.rickandmortyapi.model

import com.google.gson.annotations.SerializedName

data class CharacterList (
    @SerializedName("results")
    val results: ArrayList<Character>,
    @SerializedName("info")
    val info: InfoData
)
