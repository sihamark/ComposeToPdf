package eu.heha.applicator.model

import kotlinx.serialization.Serializable

@Serializable
sealed class DocumentPage {
    abstract val container: ContainerModel

    @Serializable
    data class CoverLetterPage(
        override val container: ContainerModel,
        val coverLetter: CoverLetterModel
    ) : DocumentPage()

    @Serializable
    data class ProfilePage(
        override val container: ContainerModel,
        val profile: ProfileModel
    ) : DocumentPage()
}

@Serializable
data class ContainerModel(
    val name: String,
    val imageUrl: String,
    val subtitle: String,
    val contactInfo: List<String>,
    val footLine: String
)

@Serializable
data class CoverLetterModel(
    val name: String,
    val address: List<String>,
    val date: String,
    val salutation: String,
    val text: String,
    val closing: String
)

@Serializable
data class ProfileModel(
    val sidePaneSections: List<ProfileSection>,
    val mainPaneSections: List<ProfileSection>
)

@Serializable
sealed interface ProfileSection {
    @Serializable
    data class ProficiencySection(
        val type: String,
        val proficiency: List<Proficiency>
    ) : ProfileSection {
        @Serializable
        data class Proficiency(val name: String, val proficiency: Float)
    }

    @Serializable
    data class ListSection(
        val type: String,
        val items: List<String>
    ) : ProfileSection

    @Serializable
    data class ContactSection(
        val type: String,
        val contacts: Map<String, String>
    ) : ProfileSection

    @Serializable
    data class HighlightSection(
        val type: String,
        val highlights: List<HighlightItem>
    ) : ProfileSection {
        @Serializable
        data class HighlightItem(
            val title: String,
            val description: String,
            val highlight: String
        )
    }
}