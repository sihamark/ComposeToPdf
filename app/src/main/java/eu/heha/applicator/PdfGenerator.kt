package eu.heha.applicator

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import eu.heha.applicator.ui.theme.ApplicatorTheme
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import java.util.*

class PdfGenerator(private val context: Context) {

    private val viewTag = "PdfGenerator_" + UUID.randomUUID().toString()

    suspend fun generateDocument(
        viewGroup: ViewGroup,
        size: DpSize = sizeA4,
        content: @Composable () -> Unit
    ) {
        Napier.i("Generating document")
        try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(
                size.width.value.toInt(),
                size.height.value.toInt(),
                1
            ).create()
            val page = document.startPage(pageInfo)
            val view = ComposeView(context).apply {
                setContent {
                    CompositionLocalProvider(LocalDensity.provides(Density(1f, 1f))) {
                        ApplicatorTheme {
                            Box(
                                contentAlignment = Alignment.TopStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(size)
                                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                                        .border(4.dp, Color.Red)
                                ) {
                                    content()
                                }
                            }
                        }
                    }
                }
                tag = viewTag
            }
            var existing: View? = viewGroup.findViewWithTag(viewTag)
            while (existing != null) {
                viewGroup.removeView(existing)
                existing = viewGroup.findViewWithTag(viewTag)
            }
            viewGroup.addView(view, 0)
            delay(500)
            view.draw(page.canvas)
            viewGroup.removeView(view)
            document.finishPage(page)
            val file = context.filesDir.resolve("test.pdf")
            file.outputStream()
                .use { document.writeTo(it) }
            document.close()
            Napier.i("finished generating document")
        } catch (e: Exception) {
            Napier.e("Error generating document", e)
        }
    }

    companion object {
        val sizeA4 = DpSize(595.dp, 842.dp)
    }
}