package edu.mirea.onebeattrue.vktestpokemon.presentation.list

import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import kotlinx.coroutines.flow.StateFlow

interface ListComponent {

    val model: StateFlow<ListStore.State>

    fun openDetails(pokemon: Pokemon)

    fun loadNextData()

    fun reloadData()
}