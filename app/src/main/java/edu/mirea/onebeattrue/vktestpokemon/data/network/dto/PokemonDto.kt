package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("name") val name: String,
    @SerializedName("abilities") val abilities: List<AbilityDto>,
    @SerializedName("cries") val cries: CryDto,
    @SerializedName("sprites") val sprites: SpriteDto,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
)