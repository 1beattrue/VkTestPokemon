package edu.mirea.onebeattrue.vktestpokemon.data.repository

import android.util.Log
import edu.mirea.onebeattrue.vktestpokemon.data.mapper.toEntities
import edu.mirea.onebeattrue.vktestpokemon.data.mapper.toEntity
import edu.mirea.onebeattrue.vktestpokemon.data.network.api.ApiService
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository {

    private var offset = 0

    override suspend fun loadPokemonList(): List<Pokemon> {
        Log.d("PokemonRepositoryImpl", "${apiService.loadPokemonList(offset, LIMIT).results}")
        return apiService.loadPokemonList(offset, LIMIT).results.map { nameDto ->
            apiService.getPokemonByName(nameDto.name)
        }.toEntities()
    }

    override suspend fun reloadPokemonList(): List<Pokemon> {
        return loadPokemonList()
    }

    override suspend fun loadNextPokemonList(): List<Pokemon>? {
        offset += 20
        return loadPokemonList().ifEmpty { null }
    }

    override suspend fun getPokemonByName(name: String): Pokemon {
        return apiService.getPokemonByName(name).toEntity()
    }

    companion object {
        private const val LIMIT = 20
    }
}