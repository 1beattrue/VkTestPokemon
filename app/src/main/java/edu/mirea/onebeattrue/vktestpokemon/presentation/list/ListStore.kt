package edu.mirea.onebeattrue.vktestpokemon.presentation.list

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.domain.usecase.LoadNextPokemonListUseCase
import edu.mirea.onebeattrue.vktestpokemon.domain.usecase.LoadPokemonListUseCase
import edu.mirea.onebeattrue.vktestpokemon.domain.usecase.ReloadPokemonListUseCase
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.Intent
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.Label
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OpenDetails(val pokemon: Pokemon) : Intent
        data object LoadNextData : Intent
        data object ReloadData : Intent
    }

    data class State(
        val list: List<Pokemon>,
        val screenState: ScreenState,
        val isNextDataLoading: Boolean,
        val isNextDataLoadingFailure: Boolean,
        val isDataReloading: Boolean
    ) {
        sealed interface ScreenState {
            data object Loading : ScreenState
            data object Failure : ScreenState

            data class Success(
                val hasNextData: Boolean
            ) : ScreenState
        }
    }

    sealed interface Label {
        data class OpenDetails(val pokemon: Pokemon) : Label
    }
}

class ListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loadPokemonListUseCase: LoadPokemonListUseCase,
    private val reloadPokemonListUseCase: ReloadPokemonListUseCase,
    private val loadNextPokemonListUseCase: LoadNextPokemonListUseCase
) {

    fun create(): ListStore =
        object : ListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ListStore",
            initialState = State(
                screenState = State.ScreenState.Loading,
                isNextDataLoading = false,
                isDataReloading = false,
                isNextDataLoadingFailure = false,
                list = listOf()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class LoadData(val list: List<Pokemon>) : Action
        data object FailureLoading : Action
    }

    private sealed interface Msg {
        data object DataLoading : Msg
        data object DataReloading : Msg
        data class DataLoaded(val list: List<Pokemon>) : Msg
        data object DataLoadedFailure : Msg
        data object NextDataLoadedFailure : Msg
        data object NextDataLoading : Msg
        data object NoNewData : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                try {
                    val pokemonList = loadPokemonListUseCase()
                    dispatch(Action.LoadData(pokemonList))
                } catch (e: Exception) {
                    Log.e("ListStore", "${e.message}")
                    dispatch(Action.FailureLoading)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.LoadNextData -> {
                    scope.launch {
                        dispatch(Msg.NextDataLoading)
                        try {
                            val oldList = getState().list
                            val nextList = loadNextPokemonListUseCase()
                            if (nextList != null) {
                                dispatch(Msg.DataLoaded(oldList + nextList))
                            } else {
                                dispatch(Msg.NoNewData)
                            }
                        } catch (e: Exception) {
                            Log.e("ListStore", "${e.message}")
                            dispatch(Msg.NextDataLoadedFailure)
                        }
                    }
                }

                is Intent.OpenDetails -> {
                    publish(Label.OpenDetails(intent.pokemon))
                }

                Intent.ReloadData -> {
                    scope.launch {
                        dispatch(Msg.DataReloading)
                        try {
                            val oldList = getState().list
                            val pokemonList = reloadPokemonListUseCase()
                            dispatch(Msg.DataLoaded(oldList + pokemonList))
                        } catch (e: Exception) {
                            Log.e("ListStore", "${e.message}")
                            dispatch(Msg.DataLoadedFailure)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                Action.FailureLoading -> {
                    dispatch(Msg.DataLoadedFailure)
                }

                is Action.LoadData -> {
                    dispatch(Msg.DataLoaded(action.list))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.DataLoaded -> copy(
                    screenState = State.ScreenState.Success(
                        hasNextData = true
                    ),
                    list = msg.list,
                    isNextDataLoading = false,
                    isDataReloading = false,
                    isNextDataLoadingFailure = false
                )

                is Msg.NoNewData -> copy(
                    screenState = State.ScreenState.Success(
                        hasNextData = false
                    ),
                    isNextDataLoading = false,
                    isNextDataLoadingFailure = false
                )

                Msg.DataLoadedFailure -> copy(
                    screenState = State.ScreenState.Failure,
                    isDataReloading = false
                )

                Msg.DataLoading -> copy(screenState = State.ScreenState.Loading)
                Msg.NextDataLoading -> copy(isNextDataLoading = true)
                Msg.DataReloading -> copy(isDataReloading = true)

                Msg.NextDataLoadedFailure -> copy(
                    isNextDataLoadingFailure = true
                )
            }
    }
}
