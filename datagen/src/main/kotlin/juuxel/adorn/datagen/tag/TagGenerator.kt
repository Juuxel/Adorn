package juuxel.adorn.datagen.tag

import juuxel.adorn.datagen.GeneratorConfig
import juuxel.adorn.datagen.GeneratorConfigLoader
import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.Material
import java.nio.file.Files
import java.nio.file.Path

class TagGenerator(private val entries: TagEntryProvider) {
    fun generate(materials: List<Material>): String {
        val entries = entries.getEntries(materials)
        val values: List<String> = entries.map { entry ->
            if (entry.isModded) {
                "{ \"id\": \"${entry.id}\", \"required\": false }"
            } else {
                "\"${entry.id}\""
            }
        }
        val joinedValues = values.joinToString(separator = ",\n    ")
        return """
            |{
            |  "replace": false,
            |  "values": [
            |    $joinedValues
            |  ]
            |}
        """.trimMargin() + '\n'
    }

    companion object {
        val GENERATORS_BY_ID: Map<Id, TagGenerator> = mapOf(
            adorn("benches") to TagGenerator(TagEntryProviders.BENCHES),
            adorn("chairs") to TagGenerator(TagEntryProviders.CHAIRS),
            adorn("coffee_tables") to TagGenerator(TagEntryProviders.COFFEE_TABLES),
            adorn("drawers") to TagGenerator(TagEntryProviders.DRAWERS),
            adorn("dyed_candlelit_lanterns") to TagGenerator(TagEntryProviders.DYED_CANDLELIT_LANTERNS),
            adorn("kitchen_counters") to TagGenerator(TagEntryProviders.KITCHEN_COUNTERS),
            adorn("kitchen_cupboards") to TagGenerator(TagEntryProviders.KITCHEN_CUPBOARDS),
            adorn("kitchen_sinks") to TagGenerator(TagEntryProviders.KITCHEN_SINKS),
            adorn("sofas") to TagGenerator(TagEntryProviders.SOFAS),
            adorn("stone_platforms") to TagGenerator(TagEntryProviders.STONE_PLATFORMS),
            adorn("stone_posts") to TagGenerator(TagEntryProviders.STONE_POSTS),
            adorn("stone_steps") to TagGenerator(TagEntryProviders.STONE_STEPS),
            adorn("tables") to TagGenerator(TagEntryProviders.TABLES),
            adorn("table_lamps") to TagGenerator(TagEntryProviders.TABLE_LAMPS),
            adorn("wooden_platforms") to TagGenerator(TagEntryProviders.WOODEN_PLATFORMS),
            adorn("wooden_posts") to TagGenerator(TagEntryProviders.WOODEN_POSTS),
            adorn("wooden_shelves") to TagGenerator(TagEntryProviders.WOODEN_SHELVES),
            adorn("wooden_steps") to TagGenerator(TagEntryProviders.WOODEN_STEPS),
            minecraft("non_flammable_wood") to TagGenerator(TagEntryProviders.NON_FLAMMABLE_WOOD),
        )

        fun generate(configs: List<Path>, outputDirectory: Path) {
            val configs = configs.asSequence()
                .sortedBy { it.toAbsolutePath() }
                .map { GeneratorConfigLoader.read(it) }
                .toList()
            generate(configs) { path, text ->
                val outputPath = outputDirectory.resolve(path)
                Files.createDirectories(outputPath.parent)
                Files.writeString(outputPath, text)
            }
        }

        fun generate(configs: List<GeneratorConfig>, consumer: (path: String, text: String) -> Unit) {
            val materials = configs.asSequence()
                .flatMap {
                    sequence {
                        yieldAll(it.woods)
                        yieldAll(it.stones)
                        yieldAll(it.wools)
                    }
                }
                .map { it.material }
                .toList()
                .distinctBy { it.id }

            for ((id, generator) in GENERATORS_BY_ID) {
                for (tagType in arrayOf("blocks", "items")) {
                    val path = "data/${id.namespace}/tags/$tagType/${id.path}.json"
                    consumer(path, generator.generate(materials))
                }
            }
        }

        private fun adorn(path: String) = Id("adorn", path)
        private fun minecraft(path: String) = Id("minecraft", path)
    }
}
