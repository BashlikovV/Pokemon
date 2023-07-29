package by.bashlikovvv.pokemon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

fun Int.getBitmapFromImage(context: Context): Bitmap {
    val db = ContextCompat.getDrawable(context, this)
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bit)
    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)

    return bit
}

suspend fun getBitmapWithGlide(url: String) = withContext(Dispatchers.IO) {
    return@withContext try {
        val result = Glide.with(DataModule.applicationContext!!)
            .asBitmap()
            .load(url)
            .transform(CenterCrop())
            .submit()
            .get()

        result ?: R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
    } catch (e: ExecutionException) {
        R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
    } catch (e: InterruptedException) {
        R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
    }
}