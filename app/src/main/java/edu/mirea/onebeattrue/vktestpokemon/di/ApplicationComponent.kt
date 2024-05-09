package edu.mirea.onebeattrue.vktestpokemon.di

import dagger.Component
import edu.mirea.onebeattrue.vktestpokemon.presentation.MainActivity

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }
}