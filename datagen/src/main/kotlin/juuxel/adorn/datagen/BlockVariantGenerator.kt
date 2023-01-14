package juuxel.adorn.datagen

import java.util.TreeMap

object BlockVariantGenerator {
    private const val TEXTURE_GENERATOR_NAME = "block_variant_textures"

    fun generateIconFile(config: GeneratorConfig): String? {
        val iconMap: MutableMap<String, BlockVariantTextures> = TreeMap()
        val allMaterials = config.woods + config.stones + config.wools

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
            )
        }

        if (iconMap.isEmpty()) return null
        return '{' + iconMap.entries.joinToString { (key, value) -> """"$key": ${value.toJson()}""" } +  '}'
    }

    private class BlockVariantTextures(val iconItem: String, val mainTexture: String) {
        fun toJson(): String = buildString {
            append("{\"icon\":{\"id\":\"")
            append(iconItem)
            append("\"},\"texture\":\"")
            append(mainTexture)
            append("\"}")
        }
    }
}
