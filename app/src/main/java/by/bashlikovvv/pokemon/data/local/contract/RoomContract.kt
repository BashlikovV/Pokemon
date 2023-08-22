package by.bashlikovvv.pokemon.data.local.contract

object RoomContract {

    const val DATABASE_NAME = "pokemon.room"

    object PageTable {
        const val TABLE_NAME = "pages"
        const val COLUMN_NAME = "name"
        const val COLUMN_SPRITE = "sprite"
        const val COLUMN_SELECTED = "selected"
        const val COLUMN_ID = "id"
    }

    object DetailsTable {
        const val TABLE_NAME = "details"
        const val COLUMN_NAME = "name"
        const val COLUMN_SPRITES = "sprites"
        const val COLUMN_TYPES = "types"
        const val COLUMN_WEIGHT_IN_HG = "weight"
        const val COLUMN_HEIGHT_IN_DM = "height"
        const val COLUMN_ID = "id"
    }
}