package eu.heha.applicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun DocumentContent() {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }
        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Yellow)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Green)
            )
        }
    }
}

@Preview(widthDp = 595, heightDp = 842)
@Composable
private fun DocumentContentPreview() {
    CompositionLocalProvider(LocalDensity.provides(Density(1f, 1f))) {
        ApplicatorTheme {
            DocumentContent()
        }
    }
}