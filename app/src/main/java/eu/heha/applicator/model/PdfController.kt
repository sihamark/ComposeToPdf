package eu.heha.applicator.model

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfController @Inject constructor() {

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    suspend fun generateDocument(
        rootLayout: ViewGroup,
        pageContents: List<@Composable () -> Unit>
    ) {
        if (_isGenerating.value) return
        _isGenerating.value = true
        PdfGenerator.generateDocument(
            rootLayout,
            PdfGenerator.PdfRequest(
                pages = pageContents.map {
                    PdfGenerator.PdfRequest.PageContent(
                        size = PdfGenerator.sizeA4,
                        content = it
                    )
                },
                outputStream = rootLayout.context.openFileOutput("document.pdf", 0)
            )
        )
        _isGenerating.value = false
    }
}