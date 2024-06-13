package eu.heha.applicator.ui.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.heha.applicator.ui.theme.ApplicatorTheme

@Composable
fun ProfileScreen(profileSections: List<ProfileSection>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSections.size) { index ->
            when (val section = profileSections[index]) {
                is ProfileSection.LanguageSection -> LanguagePane(section)
                is ProfileSection.ListSection -> ListPane(section)
                is ProfileSection.ContactSection -> ContactPane(section)
                is ProfileSection.HighlightSection -> HighlightPane(section)
            }
        }
    }
}

@Composable
fun LanguagePane(section: ProfileSection.LanguageSection) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = section.type, style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = section.language.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = { section.language.proficiency },
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}

@Composable
fun ListPane(section: ProfileSection.ListSection) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = section.type, style = MaterialTheme.typography.headlineSmall)
            section.items.forEach { item ->
                Text(text = item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ContactPane(section: ProfileSection.ContactSection) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = section.type, style = MaterialTheme.typography.headlineSmall)
            section.contacts.forEach { (platform, link) ->
                Text(
                    text = "$platform: $link",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun HighlightPane(section: ProfileSection.HighlightSection) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = section.type, style = MaterialTheme.typography.headlineSmall)
            section.highlights.forEach { highlight ->
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(text = highlight.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = highlight.description, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Highlight: ${highlight.highlight}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(
    widthDp = 595,
    heightDp = 842,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    val profileSections = listOf(
        ProfileSection.LanguageSection("Spoken Languages", Language("English", 0.9f)),
        ProfileSection.ListSection("Skills", listOf("Software Architecture", "Testing")),
        ProfileSection.ContactSection(
            "Contact Information",
            mapOf("GitHub" to "https://github.com/username")
        ),
        ProfileSection.HighlightSection(
            "Projects",
            listOf(
                HighlightItem(
                    "Project A",
                    "An e-commerce app for sustainable products.",
                    "Offline mode with Room database"
                )
            )
        )
    )
    ApplicatorTheme {
        ProfileScreen(profileSections)
    }
}

sealed interface ProfileSection {
    data class LanguageSection(
        val type: String,
        val language: Language
    ) : ProfileSection

    data class ListSection(
        val type: String,
        val items: List<String>
    ) : ProfileSection

    data class ContactSection(
        val type: String,
        val contacts: Map<String, String>
    ) : ProfileSection

    data class HighlightSection(
        val type: String,
        val highlights: List<HighlightItem>
    ) : ProfileSection
}

data class Language(val name: String, val proficiency: Float)
data class HighlightItem(val title: String, val description: String, val highlight: String)