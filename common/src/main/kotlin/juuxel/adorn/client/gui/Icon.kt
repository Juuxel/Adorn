package juuxel.adorn.client.gui

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.serialization.JsonOps
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper

sealed interface Icon {
    fun render(matrices: MatrixStack, x: Int, y: Int, itemRenderer: ItemRenderer)

    companion object {
        val MISSING = TextureIcon(Identifier("minecraft:textures/item/barrier.png"))

        fun fromJson(json: JsonElement): Icon {
            if (json is JsonObject) {
                val id = JsonHelper.getString(json, "id")

                if (!Registries.ITEM.containsId(Identifier(id))) {
                    return MISSING
                }

                if (!json.has("Count")) {
                    json.addProperty("Count", 1)
                }

                return ItemStack.CODEC.decode(JsonOps.INSTANCE, json).get().map(
                    { ItemIcon(it.first) },
                    { throw JsonParseException("Could not parse item icon: ${it.message()}") }
                )
            }

            return TextureIcon(Identifier(json.asString))
        }
    }

    class ItemIcon(private val stack: ItemStack) : Icon {
        override fun render(matrices: MatrixStack, x: Int, y: Int, itemRenderer: ItemRenderer) {
            itemRenderer.renderGuiItemIcon(stack, x, y)
        }
    }

    class TextureIcon(private val texture: Identifier) : Icon {
        override fun render(matrices: MatrixStack, x: Int, y: Int, itemRenderer: ItemRenderer) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            RenderSystem.setShaderTexture(0, texture)
            DrawableHelper.drawTexture(matrices, x, y, 0f, 0f, 16, 16, 16, 16)
        }
    }
}
