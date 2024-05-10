package edu.mirea.onebeattrue.vktestpokemon.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.presentation.details.DefaultDetailsComponent
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.DefaultListComponent
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val listComponentFactory: DefaultListComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,

    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::child,
        key = "root"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Details -> {
            val component = detailsComponentFactory.create(
                onBackClicked = { navigation.pop() },
                componentContext = componentContext,
                pokemon = config.pokemon
            )
            RootComponent.Child.Details(component)
        }

        Config.List -> {
            val component = listComponentFactory.create(
                onPokemonClicked = { pokemon ->
                    navigation.pushNew(Config.Details(pokemon))
                },
                componentContext = componentContext,
            )
            RootComponent.Child.List(component)
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Details(val pokemon: Pokemon) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}