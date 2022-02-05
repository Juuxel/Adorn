package juuxel.adorn.datagen.tag

import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.Material

interface TagEntryProvider {
    fun getEntries(materials: Collection<Material>): List<Entry>

    data class Entry(val material: Material, val id: Id, val isModded: Boolean)

    class Simple(private val blockType: String) : TagEntryProvider {
        override fun getEntries(materials: Collection<Material>): List<Entry> =
            materials.map {
                val id = Id(
                    "adorn",
                    if (it.isModded()) "${it.id.namespace}/${it.id.path}" else it.id.path
                )
                Entry(it, id.suffixed(blockType), it.isModded())
            }
    }

    class Filtered(private val parent: TagEntryProvider, private val filter: (Material) -> Boolean) : TagEntryProvider {
        override fun getEntries(materials: Collection<Material>): List<Entry> =
            parent.getEntries(materials).filter { filter(it.material) }
    }

    class Multi(private val parents: List<TagEntryProvider>) : TagEntryProvider {
        constructor(vararg parents: TagEntryProvider) : this(parents.toList())

        override fun getEntries(materials: Collection<Material>): List<Entry> =
            parents.flatMap { it.getEntries(materials) }
    }
}
