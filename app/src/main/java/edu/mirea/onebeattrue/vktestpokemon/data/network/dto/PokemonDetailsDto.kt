package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("abilities") val abilities: List<AbilityDto>,
    @SerializedName("cries") val cries: List<CryDto>,
    @SerializedName("sprites") val sprites: List<SpriteDto>,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
)