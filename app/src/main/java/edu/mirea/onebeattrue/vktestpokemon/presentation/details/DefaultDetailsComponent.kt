package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted("pokemon") override val pokemon: Pokemon,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {
    override fun onClickBack() {
        onClickBack()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pokemon") pokemon: Pokemon,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}