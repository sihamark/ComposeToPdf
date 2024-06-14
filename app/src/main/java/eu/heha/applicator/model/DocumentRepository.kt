package eu.heha.applicator.model

import io.github.aakira.napier.Napier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DocumentRepository {
    private val coverLetterPage = DocumentPage.CoverLetterPage(
        container = ContainerModel(
            name = "John Doe",
            imageUrl = "https://example.com/image.jpg",
            subtitle = "Software Developer",
            contactInfo = listOf("Email: john.doe@example.com", "Phone: 1234567890"),
            footLine = "Footer text"
        ),
        coverLetter = CoverLetterModel(
            name = "John Doe",
            address = listOf("123 Street", "City", "Country"),
            date = "2022-01-01",
            salutation = "Dear Hiring Manager,",
            text = "This is the body of the cover letter.",
            closing = "Best regards,"
        )
    )

    fun foo() {
        try {
            val jsonString = Json.encodeToString(coverLetterPage)
            Napier.e(jsonString)
            val deserializedPage = Json.decodeFromString<DocumentPage>(jsonString)
            Napier.e(deserializedPage.toString())
            val deserializedCoverLetterPage = Json
                .decodeFromString<DocumentPage>(jsonString) as DocumentPage.CoverLetterPage
            Napier.e(deserializedCoverLetterPage.toString())
        } catch (e: Exception) {
            Napier.e(e) { "Error in foo()" }
        }
    }
}