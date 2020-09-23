package ph.movieguide.com.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageUtils {

    companion object {
        // https://stackoverflow.com/a/7780289/1076574
        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        fun saveBitmap(ctx: Context, bitmap: Bitmap): File {
            val filename = "${StringUtils.createFilename()}.JPG"
            val file = File(ctx.cacheDir, filename)
            file.createNewFile()

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val bitmapData = byteArrayOutputStream.toByteArray()

            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bitmapData)
            fileOutputStream.flush()
            fileOutputStream.close()
            return file
        }
    }
}