package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.vktestpokemon.R
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {
    val pokemon = component.pokemon

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pokemon.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onClickBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 64.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ImageCard(pokemon = pokemon)
            }
            item {
                PlayAudioCard(cryUrl = pokemon.cryUrl)
            }
            item {
                CharacteristicsCard(weight = pokemon.weight, height = pokemon.height)
            }
            item {
                AbilitiesCard(abilities = pokemon.abilities)
            }
        }
    }
}

@Composable
private fun CharacteristicsCard(
    modifier: Modifier = Modifier,
    weight: Int,
    height: Int
) {
    CustomCard(modifier = modifier) {
        Text(
            text = stringResource(R.string.characteristics),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.height))
                }
                append(" $height")
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.weight))
                }
                append(" $weight")
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AbilitiesCard(
    modifier: Modifier = Modifier,
    abilities: List<String>
) {
    CustomCard(modifier = modifier) {
        Text(
            text = stringResource(R.string.abilities),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light
        )
        abilities.forEach { ability ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = ability,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    CustomCard(modifier = modifier) {
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
    }
}

@Composable
private fun PlayAudioCard(
    modifier: Modifier = Modifier,
    cryUrl: String
) {
    val scope = rememberCoroutineScope()
    val audioPlayer = AudioPlayer()

    CustomCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cry),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Light
            )
            IconButton(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        audioPlayer.startPlaying(cryUrl)
                    }
                }
            ) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
            }
        }
    }
}

@Composable
private fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color.White)
    ) {
        Card(
            modifier = modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}