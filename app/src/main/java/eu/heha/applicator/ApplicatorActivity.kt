package eu.heha.applicator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import eu.heha.applicator.model.ImageController
import eu.heha.applicator.model.PdfController
import eu.heha.applicator.ui.App
import eu.heha.applicator.ui.document.ApplicationDocumentContainer
import eu.heha.applicator.ui.document.ApplicationDocumentCoverLetter
import eu.heha.applicator.ui.document.defaultContainerModel
import eu.heha.applicator.ui.document.defaultCoverLetterModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApplicatorActivity : ComponentActivity() {

    @Inject
    lateinit var imageController: ImageController

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
                        generateDocument = { generateDocument(rootLayout) },
                        openDocument = ::openDocument
                    )
                }
            }
        )
        setContentView(rootLayout)
    }

    private fun openDocument(uri: Uri) {
        startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(uri, contentResolver.getType(uri))
            }
        )
    }

    private fun generateDocument(rootLayout: FrameLayout) {
        lifecycleScope.launch {
            val containerModel = defaultContainerModel()
            val coverLetterModel = defaultCoverLetterModel()

            imageController.loadImage(containerModel.imageUrl)

            pdfController.generateDocument(
                rootLayout,
                pageContents = listOf(
                    {
                        ApplicationDocumentContainer(model = containerModel) {
                            ApplicationDocumentCoverLetter(model = coverLetterModel)
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