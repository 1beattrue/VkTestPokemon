package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonListDto(
    @SerializedName("results") val results: List<PokemonNameDto>,
    @SerializedName("count") val count: Int
)