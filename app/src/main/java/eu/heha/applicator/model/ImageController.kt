package eu.heha.applicator.model

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageController @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun loadImage(imageUrl: String) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size(500, 500))
            .build()
        context.imageLoader.enqueue(request).job.await()
    }

}