package edu.mirea.onebeattrue.vktestpokemon.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.vktestpokemon.data.network.api.ApiFactory
import edu.mirea.onebeattrue.vktestpokemon.data.network.api.ApiService
import edu.mirea.onebeattrue.vktestpokemon.data.repository.PokemonRepositoryImpl
import edu.mirea.onebeattrue.vktestpokemon.domain.repository.PokemonRepository

@Module
interface DataModule {
    @[Binds ApplicationScope]
    fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository

    companion object {
        @[Provides ApplicationScope]
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}
