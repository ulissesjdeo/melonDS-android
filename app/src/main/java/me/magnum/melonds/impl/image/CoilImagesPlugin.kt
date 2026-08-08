package me.magnum.melonds.impl.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableLoader
import io.noties.markwon.image.AsyncDrawableScheduler
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageSpanFactory
import org.commonmark.node.Image

/** Markwon image support backed by the app's existing Coil 2 image loader. */
class CoilImagesPlugin(
    context: Context,
    imageLoader: ImageLoader,
) : AbstractMarkwonPlugin() {

    private val drawableLoader = CoilAsyncDrawableLoader(context.applicationContext, imageLoader)

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.asyncDrawableLoader(drawableLoader)
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(Image::class.java, ImageSpanFactory())
    }

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        AsyncDrawableScheduler.unschedule(textView)
    }

    override fun afterSetText(textView: TextView) {
        AsyncDrawableScheduler.schedule(textView)
    }
}

private class CoilAsyncDrawableLoader(
    private val context: Context,
    private val imageLoader: ImageLoader,
) : AsyncDrawableLoader() {

    private val activeDrawables = mutableSetOf<AsyncDrawable>()
    private val requests = mutableMapOf<AsyncDrawable, Disposable>()

    override fun load(drawable: AsyncDrawable) {
        cancel(drawable)
        activeDrawables += drawable

        val request = ImageRequest.Builder(context)
            .data(drawable.destination)
            .target(
                onSuccess = { result -> deliver(drawable, result) },
                onError = { result -> deliver(drawable, result) },
            )
            .build()
        val disposable = imageLoader.enqueue(request)

        if (drawable in activeDrawables) {
            requests[drawable] = disposable
        } else {
            disposable.dispose()
        }
    }

    override fun cancel(drawable: AsyncDrawable) {
        activeDrawables.remove(drawable)
        requests.remove(drawable)?.dispose()
    }

    override fun placeholder(drawable: AsyncDrawable): Drawable? = null

    private fun deliver(asyncDrawable: AsyncDrawable, result: Drawable?) {
        requests.remove(asyncDrawable)
        if (activeDrawables.remove(asyncDrawable) && asyncDrawable.isAttached && result != null) {
            DrawableUtils.applyIntrinsicBoundsIfEmpty(result)
            asyncDrawable.setResult(result)
        }
    }
}
