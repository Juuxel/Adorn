package juuxel.adorn.book

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonPrimitive
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import juuxel.adorn.gui.widget.WBigLabel
import juuxel.adorn.util.JanksonOps
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

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
            Jankson.builder()
                // TODO: In 1.15 and J-Fabric 2.0.0, include text arrays
                .registerTypeAdapter(Text::class.java, ::readText)
                .registerPrimitiveTypeAdapter(Text::class.java) { readText(JsonPrimitive(it)) }
                .registerTypeFactory(Book::class.java) { Book(MISSINGNO, MISSINGNO, MISSINGNO, ArrayList()) }
                .registerTypeFactory(Page::class.java) { Page(ArrayList(), MISSINGNO, MISSINGNO) }
                .registerPrimitiveTypeAdapter(Item::class.java) { Registry.ITEM.getOrEmpty(Identifier(it.toString())).orElse(Items.AIR) }
    }
}
