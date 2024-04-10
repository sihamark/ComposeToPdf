package eu.heha.applicator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun App(generateDocument: () -> Unit) {
    ApplicatorTheme {
        Scaffold { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(text = "This is for generating a pdf")
                Button(onClick = generateDocument) {
                    Text(text = "generate document")
                }
            }
        }
    }
}