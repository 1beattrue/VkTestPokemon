package edu.mirea.onebeattrue.vktestpokemon.presentation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    component: ListComponent
) {
    val state by component.model.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(state.isDataReloading, { component.reloadData() })

    Text(text = "aboba")

    Scaffold {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

            }
//            when (val screenState = state.screenState) {
//                ListStore.State.ScreenState.Failure -> {
//                    LazyColumn(
//                        modifier = Modifier.padding(paddingValues)
//                    ) {
//
//                    }
//                }
//
//                ListStore.State.ScreenState.Loading -> {
//
//                }
//
//                is ListStore.State.ScreenState.Success -> {
//
//                }
//            }

            PullRefreshIndicator(
                state.isDataReloading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}