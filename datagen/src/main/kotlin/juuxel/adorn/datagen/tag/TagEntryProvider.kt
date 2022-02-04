package juuxel.adorn.datagen.tag

import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.Material

interface TagEntryProvider {
    fun getEntries(materials: Collection<Material>): Map<Material, Entry>

    data class Entry(val id: Id, val isModded: Boolean)

    class Simple(private val blockType: String) : TagEntryProvider {
        override fun getEntries(materials: Collection<Material>): Map<Material, Entry> =
            materials.associateWith {
                val id = if (!it.isModded()) it.id.copy(namespace = "adorn") else it.id
                Entry(id.suffixed(blockType), it.isModded())
            }
    }

    class Filtered(private val parent: TagEntryProvider, private val filter: (Material) -> Boolean) : TagEntryProvider {
        override fun getEntries(materials: Collection<Material>): Map<Material, Entry> =
            parent.getEntries(materials).filterKeys(filter)
    }

    class Multi(private val parents: List<TagEntryProvider>) : TagEntryProvider {
        constructor(vararg parents: TagEntryProvider) : this(parents.toList())

        override fun getEntries(materials: Collection<Material>): Map<Material, Entry> {
            val result = mutableMapOf<Material, Entry>()
            for (parent in parents) result += parent.getEntries(materials)
            return result
        }
    }
}
