package juuxel.adorn.datagen

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory

object GeneratorConfigLoader {
    fun read(path: Path): GeneratorConfig {
        val document = Files.newInputStream(path).use { DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it) }
        val root = document.documentElement
        check(root.tagName == Tags.ROOT) { "generator config root element must be ${Tags.ROOT}, found ${root.tagName}" }
        val woods = root.getElementSequenceByTagName(Tags.WOOD).map(::readWood).toSet()
        val stones = root.getElementSequenceByTagName(Tags.STONE).map(::readStone).toSet()
        val wools = if (root.getAttribute(Attributes.WOOL).toBoolean()) WoolMaterial.values() else emptyArray()
        val conditionType = ConditionType.parse(root.getAttribute(Attributes.CONDITION_TYPE))
            ?: error("Unknown condition type in $path: ${root.getAttribute(Attributes.CONDITION_TYPE)}")
        val rootReplacements = getReplacements(root)
        return GeneratorConfig(
            woods, stones,
            wools.mapTo(LinkedHashSet()) {
                GeneratorConfig.MaterialEntry(it, exclude = emptySet(), replace = emptyMap())
            },
            conditionType,
            rootReplacements,
        )
    }

    private fun getReplacements(element: Element): Map<String, String> =
        buildSubstitutions {
            for (replaceElement in element.getElementSequenceByTagName(Tags.REPLACE)) {
                val key = replaceElement.getAttribute(Attributes.KEY)
                val value = replaceElement.getAttribute(Attributes.WITH)
                val id = replaceElement.getAttribute(Attributes.ID).toBoolean()
                if (id) key withId value else key with value
            }
        }

    private fun <M : Material> readMaterialEntry(element: Element, material: M): GeneratorConfig.MaterialEntry<M> {
        val exclude = element.getElementSequenceByTagName(Tags.EXCLUDE).mapTo(LinkedHashSet()) {
            it.getAttribute(Attributes.GENERATOR)
        }
        val replace = getReplacements(element)
        return GeneratorConfig.MaterialEntry(material, exclude, replace)
    }

    private fun readWood(element: Element): GeneratorConfig.MaterialEntry<WoodMaterial> {
        val id = Id.parse(element.getAttribute(Attributes.ID))
        // toBoolean is luckily false by default (so also for empty strings)
        val fungus = element.getAttribute(Attributes.FUNGUS).toBoolean()
        val nonFlammable = element.getAttribute(Attributes.NON_FLAMMABLE).toBoolean()
        return readMaterialEntry(element, WoodMaterial(id, fungus = fungus, nonFlammable = nonFlammable))
    }

    private fun readStone(element: Element): GeneratorConfig.MaterialEntry<StoneMaterial> {
        val id = Id.parse(element.getAttribute(Attributes.ID))
        val bricks = element.getAttribute(Attributes.BRICKS).toBoolean()
        val sidedTexture = element.getAttribute(Attributes.SIDED_TEXTURE).toBoolean()
        return readMaterialEntry(element, StoneMaterial(id, bricks = bricks, hasSidedTexture = sidedTexture))
    }

    private operator fun NodeList.iterator(): Iterator<Node> =
        iterator {
            for (i in 0 until length) {
                yield(item(i))
            }
        }

    private fun Element.getElementSequenceByTagName(tag: String): Sequence<Element> =
        Sequence {
            getElementsByTagName(tag).iterator()
        }.filterIsInstance<Element>()

    private object Tags {
        const val ROOT = "data_generators"
        const val WOOD = "wood"
        const val STONE = "stone"
        const val REPLACE = "replace"
        const val EXCLUDE = "exclude"
    }

    private object Attributes {
        const val CONDITION_TYPE = "condition_type"
        const val WOOL = "wool"
        const val ID = "id"
        const val FUNGUS = "fungus"
        const val NON_FLAMMABLE = "non_flammable"
        const val BRICKS = "bricks"
        const val SIDED_TEXTURE = "sided_texture"
        const val GENERATOR = "generator"
        const val KEY = "key"
        const val WITH = "with"
    }
}
