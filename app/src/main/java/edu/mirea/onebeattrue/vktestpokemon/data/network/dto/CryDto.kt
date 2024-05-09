package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class CryDto(
    @SerializedName("latest") val cryUrl: String
)