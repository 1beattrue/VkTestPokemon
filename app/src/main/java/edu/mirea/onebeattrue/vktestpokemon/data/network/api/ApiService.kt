package edu.mirea.onebeattrue.vktestpokemon.data.network.api

import edu.mirea.onebeattrue.vktestpokemon.data.network.dto.PokemonDto
import edu.mirea.onebeattrue.vktestpokemon.data.network.dto.PokemonListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun loadPokemonList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): PokemonListDto

    @GET("pokemon/{id}")
    suspend fun getPokemonById(
        @Path("name") name: String
    ): PokemonDto
}