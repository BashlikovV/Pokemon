package by.bashlikovvv.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.bashlikovvv.pokemon.data.local.model.PokemonDetailsEntity
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.DetailsTable

@Dao
interface PokemonDetailsDao {

    @Query("SELECT * FROM ${DetailsTable.TABLE_NAME} WHERE ${DetailsTable.COLUMN_ID} = :id")
    suspend fun selectDetails(id: Int): PokemonDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(detailsEntity: PokemonDetailsEntity)

}