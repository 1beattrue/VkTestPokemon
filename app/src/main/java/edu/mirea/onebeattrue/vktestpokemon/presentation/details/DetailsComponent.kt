package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon

interface DetailsComponent {

    val pokemon: Pokemon

    fun onClickBack()

    fun playCry()
}