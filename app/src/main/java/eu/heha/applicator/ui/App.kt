package eu.heha.applicator.ui

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
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun App(generateDocument: () -> Unit) {
    val model: RootViewModel = viewModel()
    val isGenerating by model.isGenerating.collectAsState()

    ApplicatorTheme {
        Scaffold { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                val text = if (isGenerating) {
                    "is generating document"
                } else {
                    "This is for generating a pdf"
                }
                Text(text = text)

                if (isGenerating) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = generateDocument) {
                        Text(text = "generate document")
                    }
                }
            }
        }
    }
}