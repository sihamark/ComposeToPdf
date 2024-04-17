package eu.heha.applicator

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import eu.heha.applicator.model.PdfController
import eu.heha.applicator.ui.App
import eu.heha.applicator.ui.document.ApplicationDocumentContainer
import eu.heha.applicator.ui.document.defaultContainerModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApplicatorActivity : ComponentActivity() {

    @Inject
    lateinit var pdfController: PdfController

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
        lifecycleScope.launch {
            val containerModel = defaultContainerModel()
            pdfController.generateDocument(
                rootLayout,
                pageContents = listOf(
                    {
                        ApplicationDocumentContainer(model = containerModel) {
                            Text("Hello There")
                        }
                    },
                    {
                        ApplicationDocumentContainer(model = containerModel) {
                            Text("General Kenobi")
                        }
                    }
                )
            )
        }
    }
}