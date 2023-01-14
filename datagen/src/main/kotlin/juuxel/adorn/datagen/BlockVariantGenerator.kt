package juuxel.adorn.datagen

import juuxel.adorn.datagen.io.JsonOutput
import java.util.TreeMap

object BlockVariantGenerator {
    private const val TEXTURE_GENERATOR_NAME = "block_variant_textures"

    fun generateIconFile(config: GeneratorConfig): String? {
        val iconMap: MutableMap<String, Any?> = TreeMap()
        val allMaterials = config.woods + config.stones + config.wools + config.genericMaterials

        for ((material, exclude, replace) in allMaterials) {
            if (TEXTURE_GENERATOR_NAME in exclude) continue

            val substitutions = buildSubstitutions {
                "id" with material.id
                "block_variant_id" with "<id>"
                "wood_texture_separator" with "_"
                putAll(config.rootReplacements)
                init(material)
                putAll(replace)
            }

            iconMap[substitutions["block_variant_id"]!!] = BlockVariantTextures(
                iconItem = substitutions["planks"]!!,
                mainTexture = substitutions["main-texture"]!!
            ).asMap()
        }

        if (iconMap.isEmpty()) return null
        val output = JsonOutput()
        output.print(iconMap)
        return output.toString()
    }

    private class BlockVariantTextures(val iconItem: String, val mainTexture: String) {
        fun asMap(): Map<String, *> = mapOf(
            "icon" to mapOf("id" to iconItem),
            "texture" to mainTexture
        )
    }
}
