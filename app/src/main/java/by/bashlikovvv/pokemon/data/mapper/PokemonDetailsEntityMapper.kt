package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.local.model.PokemonDetailsEntity
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.Sprites

class PokemonDetailsEntityMapper : Mapper<PokemonDetailsEntity, PokemonDetails> {
    override fun mapFromEntity(entity: PokemonDetailsEntity): PokemonDetails {
        return PokemonDetails(
            id = entity.id,
            name = entity.name,
            sprites = Sprites(entity.sprites.associateBy { "" }),
            types = entity.types,
            weightInHg = entity.weightInHg,
            heightInDm = entity.heightInDm
        )
    }

    override fun mapToEntity(domain: PokemonDetails): PokemonDetailsEntity {
        val sprites = mutableListOf<String>()
        domain.sprites.sprites.forEach {
            if (it.value != null) {
                sprites.add(it.value!!)
            }
        }

        return PokemonDetailsEntity(
            id = domain.id,
            name = domain.name,
            sprites = sprites,
            types = domain.types,
            weightInHg = domain.weightInHg,
            heightInDm = domain.heightInDm
        )
    }
}