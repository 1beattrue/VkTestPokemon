package edu.mirea.onebeattrue.vktestpokemon.domain.repository

import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon

interface PokemonRepository {

    suspend fun loadPokemonList(): List<Pokemon>

    suspend fun loadNextPokemonList(): List<Pokemon>

    suspend fun getPokemonByName(name: String): Pokemon
}