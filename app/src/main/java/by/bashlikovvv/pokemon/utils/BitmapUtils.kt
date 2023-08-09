package by.bashlikovvv.pokemon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import by.bashlikovvv.pokemon.presentation.App

fun Int.getBitmapFromImage(context: Context = App.instance): Bitmap {
    val db = ContextCompat.getDrawable(context, this)
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bit)
    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)

    return bit
}