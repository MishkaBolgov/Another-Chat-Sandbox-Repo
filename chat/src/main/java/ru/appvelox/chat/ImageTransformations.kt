package ru.appvelox.chat

import android.graphics.*
import com.squareup.picasso.Transformation
import android.R.attr.bitmap
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.R.attr.bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.BitmapShader
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth





class CircularAvatar: Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(
            squaredBitmap,
            Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}


/*abstract*/ class ImageTransformation(val radius: Float): Transformation{
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)

        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = 80f
        canvas.drawRoundRect(RectF(0f, 0f, size.toFloat(), size.toFloat()), r, r, paint)
//        drawCorner(r, canvas, paint)
        canvas.drawRect(size - r, 0f, size.toFloat(), r, paint)

        squaredBitmap.recycle()
        return bitmap
    }
    override fun key() = "incoming_rounded"

//    abstract fun drawCorner(r: Float, canvas: Canvas, paint: Paint)

}

//class IncomingImageTransformation(radius: Float): ImageTransformation(radius){
//    override fun drawCorner(r: Float, canvas: Canvas, paint: Paint) {
//        canvas.drawRect(0f, 0f, r, r, paint)
//    }
//    override fun key() = "incoming_rounded"
//}
//
//class OutgoingImageTransformation(radius: Float): ImageTransformation(radius){
//    override fun drawCorner(r: Float, canvas: Canvas, paint: Paint) {
//        canvas.drawRect(r, 0f, 0f, r, paint)
//    }
//    override fun key() = "outgoing_rounded"
//}



class RoundedImage(val radius: Float): Transformation {
    override fun transform(source: Bitmap): Bitmap {

        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = 20f
        canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), r, r, paint)

        squaredBitmap.recycle()
        return bitmap

    }

    override fun key(): String {
        return "rounded"
    }
}