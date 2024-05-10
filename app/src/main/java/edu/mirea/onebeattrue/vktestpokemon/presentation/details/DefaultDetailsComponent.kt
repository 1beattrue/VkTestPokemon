package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted("pokemon") override val pokemon: Pokemon,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {
    override fun onClickBack() {
        onBackClicked()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pokemon") pokemon: Pokemon,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}