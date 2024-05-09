package edu.mirea.onebeattrue.vktestpokemon.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val backImageUrl: String,
    val frontImageUrl: String,
    val cryUrl: String,
    val abilities: List<String>
)