package by.bashlikovvv.pokemon.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.DetailsTable
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.PageTable
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity

@Dao
interface PokemonPageDao {

    @Query("SELECT * FROM ${PageTable.TABLE_NAME}")
    fun selectItemsOnline(): PagingSource<Int, PokemonItemEntity>

    @Transaction
    @Query("SELECT * " +
           "FROM ${PageTable.TABLE_NAME} " +
           "WHERE ${PageTable.COLUMN_ID} " +
           "IN (SELECT ${DetailsTable.COLUMN_ID} FROM ${DetailsTable.TABLE_NAME})")
    fun selectItemsOffline(): PagingSource<Int, PokemonItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(itemEntity: PokemonItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(itemEntities: List<PokemonItemEntity>)
}