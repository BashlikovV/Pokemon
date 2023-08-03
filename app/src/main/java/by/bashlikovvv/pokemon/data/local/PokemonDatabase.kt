package by.bashlikovvv.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bashlikovvv.pokemon.data.local.converters.BitmapTypeConverter
import by.bashlikovvv.pokemon.data.local.converters.StringTypeConverter
import by.bashlikovvv.pokemon.data.local.dao.PokemonDetailsDao
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.local.model.PokemonDetailsEntity
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity

@Database(
    entities = [PokemonItemEntity::class, PokemonDetailsEntity::class],
    version = 1
)
@TypeConverters(BitmapTypeConverter::class, StringTypeConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract val pokemonDetailsDao: PokemonDetailsDao
    abstract val pokemonPageDao: PokemonPageDao
}