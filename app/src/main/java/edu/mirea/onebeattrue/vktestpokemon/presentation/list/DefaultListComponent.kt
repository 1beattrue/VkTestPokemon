package edu.mirea.onebeattrue.vktestpokemon.presentation.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.presentation.extentions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultListComponent @AssistedInject constructor(
    private val storeFactory: ListStoreFactory,

    @Assisted("onPokemonClicked") private val onPokemonClicked: (Pokemon) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ListComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is ListStore.Label.OpenDetails -> onPokemonClicked(it.pokemon)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ListStore.State>
        get() = store.stateFlow

    override fun openDetails(pokemon: Pokemon) {
        store.accept(ListStore.Intent.OpenDetails(pokemon))
    }

    override fun loadNextData() {
        store.accept(ListStore.Intent.LoadNextData)
    }

    override fun reloadData() {
        store.accept(ListStore.Intent.ReloadData)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onPokemonClicked") onPokemonClicked: (Pokemon) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultListComponent
    }
}