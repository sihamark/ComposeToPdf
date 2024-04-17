package eu.heha.applicator.ui.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun ApplicationDocumentCoverLetter(model: ApplicationCoverLetterModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Cover Letter",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                model.address.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = model.date,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))
        Text(
            text = model.salutation,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = model.text,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = model.closing,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

data class ApplicationCoverLetterModel(
    val name: String,
    val address: List<String>,
    val date: String,
    val salutation: String,
    val text: String,
    val closing: String
)

fun defaultCoverLetterModel() = ApplicationCoverLetterModel(
    name = "John Doe",
    address = listOf("1234 Elm Street", "Anytown, USA"),
    date = "January 1, 2022",
    salutation = "Dear Hiring Manager,",
    text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
        
        At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.
    """.trimIndent(),
    closing = "Sincerely,"
)

@Preview(
    widthDp = 595,
    heightDp = 842,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
private fun CoverLetterPreview() {
    ApplicatorTheme {
        ApplicationDocumentContainer(model = defaultContainerModel()) {
            ApplicationDocumentCoverLetter(
                defaultCoverLetterModel()
            )
        }
    }
}