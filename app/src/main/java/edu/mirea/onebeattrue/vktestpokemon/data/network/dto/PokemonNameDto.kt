package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonNameDto(
    @SerializedName("name") val name: String
)