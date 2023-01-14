package juuxel.adorn.client.resources

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.minecraft.util.Identifier

data class BlockVariantTextures(val icon: BlockVariantIcon, val mainTexture: Identifier) {
    companion object {
        fun fromJson(json: JsonObject): BlockVariantTextures {
            val icon = BlockVariantIcon.fromJson(json.get("icon") ?: throw JsonParseException("Missing required key 'icon'"))
            val mainTexture = Identifier((json.get("texture") ?: throw JsonParseException("Missing required key 'texture'")).asString)
            return BlockVariantTextures(icon, mainTexture)
        }
    }
}
