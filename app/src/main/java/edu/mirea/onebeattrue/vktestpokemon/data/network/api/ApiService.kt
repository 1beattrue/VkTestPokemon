package edu.mirea.onebeattrue.vktestpokemon.data.network.api

import edu.mirea.onebeattrue.vktestpokemon.data.network.dto.PokemonDto
import edu.mirea.onebeattrue.vktestpokemon.data.network.dto.PokemonListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun loadPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListDto

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): PokemonDto
}