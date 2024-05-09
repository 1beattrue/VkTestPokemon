package edu.mirea.onebeattrue.vktestpokemon.domain.usecase

import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.domain.repository.PokemonRepository
import javax.inject.Inject

data class ReloadPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(): List<Pokemon> {
        return pokemonRepository.reloadPokemonList()
    }
}