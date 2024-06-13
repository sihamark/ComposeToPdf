package eu.heha.applicator.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.heha.applicator.model.PdfController
import eu.heha.applicator.model.PdfController.DocumentResult.Error
import eu.heha.applicator.model.PdfController.DocumentResult.Generating
import eu.heha.applicator.model.PdfController.DocumentResult.Success
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun App(
    generateDocument: () -> Unit,
    openDocument: (Uri) -> Unit
) {
    val model: RootViewModel = viewModel()
    val documentResult by model.documentResult.collectAsState()

    ApplicatorTheme {
        Scaffold { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                val text = when (documentResult) {
                    Generating -> "Generating document..."
                    is Error -> "Error generating document"
                    is Success -> "Document generated successfully"
                    null -> "Ready to generate document"
                }
                Text(text = text)

                Body(
                    documentResult = documentResult,
                    onClickOpenDocument = openDocument
                )

                if (documentResult != Generating) {
                    Button(onClick = generateDocument) {
                        Text("Generate document")
                    }
                }
            }
        }
    }
}

@Composable
private fun Body(
    documentResult: PdfController.DocumentResult?,
    onClickOpenDocument: (Uri) -> Unit
) {
    when (documentResult) {
        Generating -> CircularProgressIndicator()
        is Error -> Text(documentResult.message)
        is Success -> Button(onClick = { onClickOpenDocument(documentResult.fileUri) }) {
            Text("Open document")
        }

        null -> Unit
    }
}