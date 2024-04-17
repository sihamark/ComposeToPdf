package eu.heha.applicator.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.heha.applicator.model.PdfController
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    pdfController: PdfController
) : ViewModel() {
    val isGenerating = pdfController.isGenerating
}
