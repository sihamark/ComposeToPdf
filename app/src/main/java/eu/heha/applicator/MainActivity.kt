package eu.heha.applicator

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import eu.heha.applicator.ui.theme.ApplicatorTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var rootLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        rootLayout = FrameLayout(this)
        rootLayout.addView(
            ComposeView(this).apply {
                setContent {
                    App(generateDocument = ::generateDocument)
                }
            }
        )
        rootLayout.addView(View(this), 0)
        setContentView(rootLayout)
    }

    private fun generateDocument() {
        lifecycleScope.launch {
            Log.i("PDF", "Generating document")
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
                                DocumentContent()
                            }
                        }
                    }
                }
            })
            rootLayout.addView(view, 0)
            rootLayout.removeViewAt(1)
            delay(500)
            view.draw(page.canvas)
            document.finishPage(page)
            val file = filesDir.resolve("test.pdf")
            file.outputStream()
                .use { document.writeTo(it) }
            document.close()
            Log.i("PDF", "finished generating document")
        }
    }

    @Composable
    private fun DocumentContent() {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()) {
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
                    .fillMaxWidth()) {
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