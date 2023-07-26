package by.bashlikovvv.pokemon.data.mapper

import android.content.Context
import android.graphics.Bitmap
import by.bashlikovvv.pokemon.data.remote.response.PokemonDetailsDto
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonDetailsDtoMapper(
    private val context: Context
) {
    suspend fun mapFromEntity(entity: PokemonDetailsDto): PokemonDetails {
        val sprites = getSprites(entity.spritesDto)
        val typeDtoMapper = TypeDtoMapper()

        return PokemonDetails(
            id = entity.id.toLong(),
            name = entity.name,
            sprites = sprites,
            types = typeDtoMapper.mapFromEntity(entity.types),
            weightInHg = entity.weight,
            heightInDm = entity.height
        )
    }

    fun mapToEntity(domain: PokemonDetails): PokemonDetailsDto {
        throw UnsupportedOperationException("Not implemented")
    }

    private suspend fun getBitmapWithGlide(url: String) = withContext(Dispatchers.IO) {
        return@withContext Glide.with(context)
            .asBitmap()
            .load(url)
            .transform(CenterCrop())
            .submit()
            .get()
    }

    private suspend fun getSprites(spritesDto: SpritesDto): Sprites {
        val sprites = mutableMapOf<String, Bitmap>()
        sprites[SpriteNames.BackDefault().name] = getBitmapWithGlide(spritesDto.backDefault)
        sprites[SpriteNames.BackFemale().name] = getBitmapWithGlide(spritesDto.backFemale)
        sprites[SpriteNames.BackShiny().name] = getBitmapWithGlide(spritesDto.backShiny)
        sprites[SpriteNames.BackShinyFemale().name] = getBitmapWithGlide(spritesDto.backShinyFemale)
        sprites[SpriteNames.FrontDefault().name] = getBitmapWithGlide(spritesDto.frontDefault)
        sprites[SpriteNames.FrontFemale().name] = getBitmapWithGlide(spritesDto.frontFemale)
        sprites[SpriteNames.FrontShiny().name] = getBitmapWithGlide(spritesDto.frontShiny)
        sprites[SpriteNames.FrontShinyFemale().name] = getBitmapWithGlide(spritesDto.frontShinyFemale)

        return Sprites(sprites)
    }
}