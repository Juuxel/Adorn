package juuxel.adorn.book

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import io.github.cottonmc.jankson.JanksonFactory
import juuxel.adorn.gui.widget.WBigLabel
import juuxel.adorn.util.JanksonOps
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
data class Book(
    val title: Text,
    val subtitle: Text = LiteralText(""),
    val author: Text,
    val pages: List<Page>,
    val titleScale: Float = WBigLabel.DEFAULT_SCALE
) {
    companion object {
        private val MISSINGNO: Text = LiteralText("missingno")

        private fun readText(json: JsonElement): Text? =
            Text.Serializer.fromJson(Dynamic.convert(JanksonOps, JsonOps.INSTANCE, json))

        fun jankson(): Jankson.Builder =
            JanksonFactory.builder()
                .registerTypeFactory(Book::class.java) { Book(MISSINGNO, MISSINGNO, MISSINGNO, ArrayList()) }
                .registerTypeFactory(Page::class.java) { Page(ArrayList(), MISSINGNO, MISSINGNO) }
                .registerDeserializer(JsonElement::class.java, Text::class.java) { json, _ -> readText(json) }
    }
}
