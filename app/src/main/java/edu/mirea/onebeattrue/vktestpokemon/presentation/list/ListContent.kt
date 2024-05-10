package edu.mirea.onebeattrue.vktestpokemon.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.vktestpokemon.R
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon


@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    component: ListComponent
) {
    val state by component.model.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(state.isReloading, { component.reloadData() })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.list_app_bar_title),
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.list, key = { it.id }) { pokemon ->
                    PokemonCard(
                        modifier = Modifier.animateItemPlacement(),
                        pokemon = pokemon
                    ) {
                        component.openDetails(pokemon)
                    }
                }
                loadNext(
                    hasNextData = state.hasNextData,
                    isNextDataLoading = state.isLoading,
                    isNextDataLoadingFailure = state.isFailure,
                    onLoadNextClick = { component.loadNextData() },
                    onReloadClick = { component.reloadData() }
                )
            }

            PullRefreshIndicator(
                state.isReloading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


private fun LazyGridScope.loadNext(
    hasNextData: Boolean,
    isNextDataLoading: Boolean,
    isNextDataLoadingFailure: Boolean,
    onLoadNextClick: () -> Unit,
    onReloadClick: () -> Unit
) {
    item(
        span = { GridItemSpan(2) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (hasNextData) {
                AnimatedVisibility(
                    visible = isNextDataLoadingFailure
                ) {
                    Failure()
                }

                if (isNextDataLoading) {
                    CircularProgressIndicator()
                } else {
                    if (isNextDataLoadingFailure) {
                        OutlinedButton(
                            onClick = { onReloadClick() }
                        ) {
                            Text(text = stringResource(R.string.refresh))
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onLoadNextClick() }
                        ) {
                            Text(text = stringResource(R.string.load_more))
                        }
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.the_end),
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun Failure(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.error_text),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PokemonCard(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    onPokemonClick: () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color.White)
    ) {
        ElevatedCard(
            modifier = modifier
                .fillMaxSize(),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 16.dp,
                pressedElevation = 4.dp,
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = { onPokemonClick() }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val imageUrl = pokemon.frontImageUrl
                    ?: pokemon.backImageUrl
                    ?: pokemon.frontShinyUrl
                    ?: pokemon.backShinyUrl

                if (imageUrl != null) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.vk),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}