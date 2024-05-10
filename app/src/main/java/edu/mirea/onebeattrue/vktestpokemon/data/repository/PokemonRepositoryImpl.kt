package edu.mirea.onebeattrue.vktestpokemon.data.repository

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
    private val pokemonSet: MutableSet<Pokemon> = mutableSetOf()

    override suspend fun loadPokemonList(): List<Pokemon> {
        val loadedData = apiService.loadPokemonList(offset, LIMIT).results.map { nameDto ->
            apiService.getPokemonByName(nameDto.name)
        }.toEntities()
        pokemonSet.addAll(loadedData)
        return pokemonSet.toList()
    }

    override suspend fun loadNextPokemonList(): List<Pokemon> {
        offset += LIMIT
        return loadPokemonList()
    }

    override suspend fun getPokemonByName(name: String): Pokemon {
        return apiService.getPokemonByName(name).toEntity()
    }

    companion object {
        private const val LIMIT = 800
    }
}