package eu.heha.applicator

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import eu.heha.applicator.ui.theme.ApplicatorTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ActualContent()
        }
    }

    @Composable
    private fun ActualContent() {
        ApplicatorTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Greeting(name = "Android")
                    Button(onClick = ::generateDocument) {
                        Text(text = "generate document")
                    }
                }
            }
        }
    }

    private fun generateDocument() {
        lifecycleScope.launch {
            val document = PdfDocument()
            val pageInfo = PageInfo.Builder(595, 842, 1).create()
            val page = document.startPage(pageInfo)
            val view = PdfComposeView(this@MainActivity, content = {
                CompositionLocalProvider(LocalDensity.provides(Density(1f, 1f))) {
                    ApplicatorTheme {
                        Box(
                            contentAlignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 595.dp, height = 842.dp)
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .border(4.dp, Color.Red)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Box(modifier = Modifier
                                        .weight(1f)
                                        .width(30.dp)) {
                                        Greeting(name = "Android")
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .width(30.dp)
                                            .background(color = Color.Red)
                                    )
                                }
                            }
                        }
                    }
                }
            })
            setContentView(view)
            delay(500)
            view.draw(page.canvas)
            setContent { ActualContent() }
            document.finishPage(page)
            val file = filesDir.resolve("test.pdf")
            file.outputStream()
                .use { document.writeTo(it) }
            document.close()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ApplicatorTheme {
        Greeting("Android")
    }
}

class PdfComposeView(
    context: Context,
    private val content: @Composable () -> Unit
) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        content()
    }
}