package by.bashlikovvv.pokemon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.bashlikovvv.pokemon.data.local.contract.RoomContract.PageTable

@Entity(tableName = PageTable.TABLE_NAME)
data class PokemonItemEntity(
    @ColumnInfo(name = PageTable.COLUMN_NAME) val name: String,
    @ColumnInfo(name = PageTable.COLUMN_SPRITE) val sprite: String,
    @ColumnInfo(name = PageTable.COLUMN_SELECTED) val selected: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PageTable.COLUMN_ID) val id: Int
)