package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class SpriteDto(
    @SerializedName("back_default") val backUrl: String,
    @SerializedName("front_default") val frontUrl: String,
)
