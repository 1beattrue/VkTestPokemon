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
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.Intent
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.Label
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OpenDetails(val pokemon: Pokemon) : Intent

        data object LoadNextData : Intent
        data object ReloadData : Intent
    }

    data class State(
        val list: List<Pokemon>,
        val isLoading: Boolean,
        val isReloading: Boolean,
        val isFailure: Boolean,
        val hasNextData: Boolean
    )

    sealed interface Label {
        data class OpenDetails(val pokemon: Pokemon) : Label
    }
}

class ListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loadPokemonListUseCase: LoadPokemonListUseCase,
    private val loadNextPokemonListUseCase: LoadNextPokemonListUseCase
) {

    fun create(): ListStore =
        object : ListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ListStore",
            initialState = State(
                list = listOf(),
                isLoading = true,
                isReloading = false,
                isFailure = false,
                hasNextData = true
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
        data object FailureLoading : Msg
        data object NoNewData : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                try {
                    val data = withContext(Dispatchers.IO) {
                        loadPokemonListUseCase()
                    }
                    dispatch(Action.LoadData(data))
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
                        dispatch(Msg.DataLoading)
                        try {
                            val oldData = getState().list
                            val data = withContext(Dispatchers.IO) {
                                loadNextPokemonListUseCase()
                            }
                            if (oldData.toSet() != data.toSet()) {
                                dispatch(Msg.DataLoaded(data))
                            } else {
                                dispatch(Msg.NoNewData)
                            }
                        } catch (e: Exception) {
                            Log.e("ListStore", "${e.message}")
                            dispatch(Msg.FailureLoading)
                        }
                    }
                }

                Intent.ReloadData -> {
                    scope.launch {
                        dispatch(Msg.DataReloading)
                        try {
                            val data = withContext(Dispatchers.IO) {
                                loadPokemonListUseCase()
                            }
                            dispatch(Msg.DataLoaded(data))
                        } catch (e: Exception) {
                            Log.e("ListStore", "${e.message}")
                            dispatch(Msg.FailureLoading)
                        }
                    }
                }

                is Intent.OpenDetails -> {
                    publish(Label.OpenDetails(intent.pokemon))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                Action.FailureLoading -> {
                    dispatch(Msg.FailureLoading)
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
                    list = msg.list,
                    isLoading = false,
                    isReloading = false,
                    isFailure = false,
                )

                Msg.DataLoading -> copy(
                    isLoading = true
                )

                Msg.DataReloading -> copy(
                    isReloading = true
                )

                Msg.FailureLoading -> copy(
                    isLoading = false,
                    isReloading = false,
                    isFailure = true
                )

                Msg.NoNewData -> copy(
                    isLoading = false,
                    isReloading = false,
                    isFailure = false,
                    hasNextData = false
                )
            }
    }
}
