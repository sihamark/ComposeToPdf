package eu.heha.applicator.ui.document

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import eu.heha.applicator.ui.document.ProfileSection.HighlightSection.HighlightItem
import eu.heha.applicator.ui.document.ProfileSection.ProficiencySection
import eu.heha.applicator.ui.document.ProfileSection.ProficiencySection.Proficiency
import eu.heha.applicator.ui.theme.ApplicatorTheme

private const val SIDE_PANE_WEIGHT = 0.4f

@Composable
fun ApplicationDocumentProfile(
    sidePaneSections: List<ProfileSection>,
    mainPaneSections: List<ProfileSection>
) {
    Row {
        // Side Pane
        Column(
            Modifier.weight(SIDE_PANE_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sidePaneSections.forEach { section ->
                ProfileSectionCard(section)
            }
        }
        Spacer(Modifier.width(8.dp))

        // Main Pane
        Column(
            Modifier.weight(1f - SIDE_PANE_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            mainPaneSections.forEach { section ->
                ProfileSectionCard(section)
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(section: ProfileSection, modifier: Modifier = Modifier) {
    when (section) {
        is ProfileSection.ContactSection -> ContactCard(section, modifier)
        is ProfileSection.HighlightSection -> HighlightCard(section, modifier)
        is ProficiencySection -> ProficiencyCard(section, modifier)
        is ProfileSection.ListSection -> ListCard(section, modifier)
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

@Composable
private fun ProficiencyCard(
    section: ProficiencySection,
    modifier: Modifier = Modifier
) {
    SectionCard(section.type, modifier) {
        section.proficiency.forEach { proficiency ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = proficiency.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
                LinearProgressIndicator(
                    progress = { proficiency.proficiency },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ListCard(
    section: ProfileSection.ListSection,
    modifier: Modifier = Modifier
) {
    SectionCard(section.type, modifier) {
        section.items.forEach { item ->
            Text(text = item, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ContactCard(section: ProfileSection.ContactSection, modifier: Modifier = Modifier) {
    SectionCard(section.type, modifier) {
        section.contacts.forEach { (platform, link) ->
            Text(
                text = "$platform: $link",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HighlightCard(section: ProfileSection.HighlightSection, modifier: Modifier = Modifier) {
    SectionCard(section.type, modifier) {
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

@Preview(
    widthDp = 595,
    heightDp = 842,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
private fun ProfileScreenPreview() {
    ApplicatorTheme {
        ApplicationDocumentContainer(defaultContainerModel()) {
            val model = defaultProfileModel()
            ApplicationDocumentProfile(
                sidePaneSections = model.take(3),
                mainPaneSections = model.drop(3)
            )
        }
    }
}

fun defaultProfileModel() = listOf(
    ProfileSection.ContactSection(
        "Contact",
        mapOf(
            "LinkedIn" to "@MMusterman",
            "Phone" to "0111/11111111"
        )
    ),
    ProficiencySection(
        "Spoken Languages", listOf(
            Proficiency("English", 0.9f),
            Proficiency("Spanish", 0.7f),
            Proficiency("French", 0.5f)
        )
    ), ProficiencySection(
        "Programming Languages", listOf(
            Proficiency("Kotlin", 0.9f),
            Proficiency("Java", 0.7f),
            Proficiency("Python", 0.5f)
        )
    ),
    ProfileSection.ListSection("Skills", listOf("Software Architecture", "Testing")),
    ProfileSection.HighlightSection(
        "Projects",
        listOf(
            HighlightItem(
                "Project A",
                "An e-commerce app for sustainable products.",
                "Offline mode with Room database"
            ),
            HighlightItem(
                "Project B",
                "A photo app with social features and filters",
                "CameraX integration in compose"
            )
        )
    )
)

sealed interface ProfileSection {
    data class ProficiencySection(
        val type: String,
        val proficiency: List<Proficiency>
    ) : ProfileSection {
        data class Proficiency(val name: String, val proficiency: Float)
    }

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
    ) : ProfileSection {
        data class HighlightItem(
            val title: String,
            val description: String,
            val highlight: String
        )
    }
}

