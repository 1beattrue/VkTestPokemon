package edu.mirea.onebeattrue.vktestpokemon.data.mapper

import edu.mirea.onebeattrue.vktestpokemon.data.network.dto.PokemonDto
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon

fun PokemonDto.toEntity(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        weight = weight,
        height = height,
        backImageUrl = sprites.backUrl,
        frontImageUrl = sprites.frontUrl,
        backShinyUrl = sprites.backShinyUrl,
        frontShinyUrl = sprites.frontShinyUrl,
        cryUrl = cries.cryUrl,
        abilities = abilities.map {
            it.abilityName.name
        }
    )

fun List<PokemonDto>.toEntities(): List<Pokemon> = map { it.toEntity() }