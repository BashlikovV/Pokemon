package by.bashlikovvv.pokemon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.DetailsTable

@Entity(tableName = DetailsTable.TABLE_NAME)
data class PokemonDetailsEntity(
    @ColumnInfo(name = DetailsTable.COLUMN_NAME) val name: String,
    @ColumnInfo(name = DetailsTable.COLUMN_SPRITES) val sprites: List<String>,
    @ColumnInfo(name = DetailsTable.COLUMN_TYPES) val types: List<String>,
    @ColumnInfo(name = DetailsTable.COLUMN_WEIGHT_IN_HG) val weightInHg: Int,
    @ColumnInfo(name = DetailsTable.COLUMN_HEIGHT_IN_DM) val heightInDm: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DetailsTable.COLUMN_ID)
    val id: Int
)
