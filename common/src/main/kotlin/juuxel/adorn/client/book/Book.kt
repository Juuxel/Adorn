package juuxel.adorn.client.book

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

data class Book(
    val title: Text,
    val subtitle: Text,
    val author: Text,
    val pages: List<Page>,
    val titleScale: Float
) {
    companion object {
        val GSON = GsonBuilder()
            .registerTypeHierarchyAdapter(Text::class.java, JsonDeserializer { json, _, _ -> Text.Serializer.fromJson(json) ?: MISSINGNO })
            .registerTypeAdapter(Page.Icon::class.java, JsonDeserializer { json, _, _ -> Page.Icon.fromJson(json) })
            .create()

        private val MISSINGNO: Text = LiteralText("missingno")

        fun fromJson(json: JsonElement): Book =
            GSON.fromJson(json, Book::class.java)
    }
}
