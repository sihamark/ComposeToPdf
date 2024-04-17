package eu.heha.applicator

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@HiltAndroidApp
class ApplicatorApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .allowHardware(false)
        .bitmapConfig(android.graphics.Bitmap.Config.ARGB_8888)
        .build()
}