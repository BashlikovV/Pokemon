package by.bashlikovvv.pokemon.data.mapper

import android.content.Context
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.local.model.PokemonDetailsEntity
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.utils.getBitmapFromImage

class PokemonDetailsEntityMapper(
    private val context: Context
) : Mapper<PokemonDetailsEntity, PokemonDetails> {
    override fun mapFromEntity(entity: PokemonDetailsEntity): PokemonDetails {
        return PokemonDetails(
            id = entity.id,
            name = entity.name,
            sprites = Sprites(mapOf(SpriteNames.FrontShiny().name to entity.sprite)),
            types = entity.types,
            weightInHg = entity.weightInHg,
            heightInDm = entity.heightInDm
        )
    }

    override fun mapToEntity(domain: PokemonDetails): PokemonDetailsEntity {
        val sprite = domain.sprites.sprites[SpriteNames.FrontShiny().name] ?:
            R.drawable.baseline_error_24.getBitmapFromImage(context)

        return PokemonDetailsEntity(
            id = domain.id,
            name = domain.name,
            sprite = sprite,
            types = domain.types,
            weightInHg = domain.weightInHg,
            heightInDm = domain.heightInDm
        )
    }
}