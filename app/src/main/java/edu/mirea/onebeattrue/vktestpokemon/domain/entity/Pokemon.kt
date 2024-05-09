package edu.mirea.onebeattrue.vktestpokemon.domain.entity

data class Pokemon(
    val name: String,
    val weight: Int,
    val height: Int,
    val backImageUrl: String,
    val frontImageUrl: String,
    val cryUrl: String,
    val abilities: List<String>
)