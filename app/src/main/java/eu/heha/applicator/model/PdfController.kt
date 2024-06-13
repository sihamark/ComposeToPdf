package eu.heha.applicator.model

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfController @Inject constructor() {

    private val _documentResult = MutableStateFlow<DocumentResult?>(null)
    val documentResult = _documentResult.asStateFlow()

    suspend fun generateDocument(
        rootLayout: ViewGroup,
        pageContents: List<@Composable () -> Unit>
    ) {
        if (_documentResult.value == DocumentResult.Generating) return
        _documentResult.value = DocumentResult.Generating
        try {
            val documentFile = rootLayout.context.filesDir
                .resolve("documents/document.pdf")
            documentFile.parentFile?.mkdirs()

            PdfGenerator.generateDocument(
                rootLayout,
                PdfGenerator.PdfRequest(
                    pages = pageContents.map {
                        PdfGenerator.PdfRequest.PageContent(
                            size = PdfGenerator.sizeA4,
                            content = it
                        )
                    },
                    outputStream = documentFile.outputStream()
                )
            )
            val packageName = rootLayout.context.applicationContext.packageName
            _documentResult.value = DocumentResult.Success(
                FileProvider.getUriForFile(
                    rootLayout.context,
                    "$packageName.fileprovider",
                    documentFile
                )
            )
        } catch (e: Exception) {
            Napier.e(e) { "Error generating document" }
            _documentResult.value = DocumentResult.Error(e.message ?: "Unknown error")
        }
    }

    sealed interface DocumentResult {
        data object Generating : DocumentResult
        data class Success(val fileUri: Uri) : DocumentResult
        data class Error(val message: String) : DocumentResult
    }
}