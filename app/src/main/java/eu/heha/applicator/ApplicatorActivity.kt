package eu.heha.applicator

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ApplicatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val rootLayout = FrameLayout(this)
        rootLayout.addView(
            ComposeView(this).apply {
                setContent {
                    App(
                        generateDocument = { generateDocument(rootLayout) }
                    )
                }
            }
        )
        setContentView(rootLayout)
    }

    private fun generateDocument(rootLayout: FrameLayout) {
        val generator = PdfGenerator(this@ApplicatorActivity)
        lifecycleScope.launch {
            generator.generateDocument(
                rootLayout,
                content = { DocumentContent() }
            )
        }
    }
}