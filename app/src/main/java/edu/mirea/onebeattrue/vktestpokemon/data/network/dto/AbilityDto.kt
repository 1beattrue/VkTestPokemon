package edu.mirea.onebeattrue.vktestpokemon.data.network.dto

import com.google.gson.annotations.SerializedName

data class AbilityDto(
    @SerializedName("ability") val abilityName: AbilityNameDto
)