package by.bashlikovvv.pokemon.data.local.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapTypeConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        var bitmapOutputStream: ByteArrayOutputStream? = null
        try {
            bitmapOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bitmapOutputStream)
            return bitmapOutputStream.toByteArray()
        } finally {
            bitmapOutputStream?.close()
        }
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}