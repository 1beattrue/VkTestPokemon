package edu.mirea.onebeattrue.vktestpokemon.presentation

import android.app.Application
import edu.mirea.onebeattrue.vktestpokemon.di.DaggerApplicationComponent

class VkTestPokemonApp : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create()
    }
}