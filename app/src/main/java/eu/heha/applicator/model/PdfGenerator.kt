package eu.heha.applicator.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import eu.heha.applicator.ui.theme.ApplicatorTheme
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import java.io.OutputStream
import java.util.*

object PdfGenerator {

    val sizeA4 = DpSize(595.dp, 842.dp)

    private val viewTag = "PdfGenerator_" + UUID.randomUUID().toString()

    suspend fun generateDocument(
        viewGroup: ViewGroup,
        request: PdfRequest
    ) {
        Napier.i("Generating document")
        try {
            val document = PdfDocument()

            request.pages.forEachIndexed { index, pageContent ->
                document.addPage(
                    viewGroup = viewGroup,
                    pageNumber = pageContent.pageNumber ?: (index + 1),
                    size = pageContent.size,
                    content = pageContent.content
                )
            }

            request.outputStream.buffered().use { out ->
                document.writeTo(out)
            }

            document.close()
            Napier.i("finished generating document")
        } catch (e: Exception) {
            Napier.e("Error generating document", e)
        }
    }

    private suspend fun PdfDocument.addPage(
        viewGroup: ViewGroup,
        pageNumber: Int,
        size: DpSize,
        content: @Composable () -> Unit
    ) = page(size, pageNumber) { canvas ->
        val view = pdfComposeView(viewGroup.context, size, content)

        viewGroup.removeAnyExistingViews()
        viewGroup.addView(view, 0)

        delay(500)
        view.draw(canvas)

        viewGroup.removeView(view)
    }

    private fun pdfComposeView(
        context: Context,
        size: DpSize,
        content: @Composable () -> Unit
    ): View = ComposeView(context).apply {
        setContent {
            //do not use the windows density for it would change the size of the text for different devices
            CompositionLocalProvider(LocalDensity.provides(Density(1f, 1f))) {
                ApplicatorTheme {
                    //top start so that the view is on bounds of the view group
                    //the outer box will always be as big as the whole view/device
                    Box(contentAlignment = Alignment.TopStart) {
                        Box(modifier = Modifier.size(size)) {
                            content()
                        }
                    }
                }
            }
        }
        tag = viewTag
    }

    private fun ViewGroup.removeAnyExistingViews() {
        var existing: View? = findViewWithTag(viewTag)
        while (existing != null) {
            removeView(existing)
            existing = findViewWithTag(viewTag)
        }
    }

    private suspend fun PdfDocument.page(
        pageSize: DpSize,
        pageNumber: Int,
        onDraw: suspend (Canvas) -> Unit
    ) {
        val pageInfo = PdfDocument.PageInfo.Builder(
            pageSize.width.value.toInt(),
            pageSize.height.value.toInt(),
            pageNumber
        ).create()

        val page = startPage(pageInfo)
        onDraw(page.canvas)
        finishPage(page)
    }

    data class PdfRequest(
        val outputStream: OutputStream,
        val pages: List<PageContent>
    ) {
        data class PageContent(
            val content: @Composable () -> Unit,
            val size: DpSize = sizeA4,
            val pageNumber: Int? = null
        )
    }
}