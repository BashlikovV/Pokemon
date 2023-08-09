package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.utils.getBitmapFromImage

class PokemonItemEntityMapper : Mapper<PokemonItemEntity, PokemonItem> {
    override fun mapFromEntity(entity: PokemonItemEntity): PokemonItem {
        return PokemonItem(
            name = entity.name,
            id = entity.id,
            sprites = mutableMapOf(SpriteNames.FrontShiny().name to entity.sprite)
        )
    }

    override fun mapToEntity(domain: PokemonItem): PokemonItemEntity {
        return PokemonItemEntity(
            name = domain.name,
            sprite = domain.sprites[SpriteNames.FrontShiny().name] ?: R.drawable.baseline_error_24.getBitmapFromImage(),
            id = domain.id
        )
    }
}