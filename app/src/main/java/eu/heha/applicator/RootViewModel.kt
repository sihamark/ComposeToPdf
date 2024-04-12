package eu.heha.applicator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    pdfController: PdfController
) : ViewModel() {
    val isGenerating = pdfController.isGenerating
}
