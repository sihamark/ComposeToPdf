package eu.heha.applicator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    pdfGenerator: PdfGenerator
) : ViewModel() {
    val isGenerating = pdfGenerator.isGenerating
}
