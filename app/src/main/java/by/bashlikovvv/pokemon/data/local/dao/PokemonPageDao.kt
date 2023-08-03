package by.bashlikovvv.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.PageTable
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity

@Dao
interface PokemonPageDao {

    @Query("SELECT * FROM ${PageTable.TABLE_NAME}")
    suspend fun selectItemsOnline(): PokemonItemEntity

    /*@Transaction
    @Query("SELECT * FROM ${PageTable.TABLE_NAME} WHERE ${PageTable.COLUMN_ID} IN (SELECT details_id FROM details)")
    suspend fun selectItemsOffline(): List<PokemonItemEntity>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(itemEntity: PokemonItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(itemEntities: List<PokemonItemEntity>)
}