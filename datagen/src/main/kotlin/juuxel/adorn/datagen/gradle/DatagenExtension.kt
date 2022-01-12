package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.WoodMaterial
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class DatagenExtension {
    abstract val woodMaterials: SetProperty<WoodMaterial>
    abstract val stoneMaterials: SetProperty<StoneMaterial>
    abstract val conditionType: Property<ConditionType>
    abstract val woolMaterials: Property<Boolean>
    internal val exclusions: MutableMap<Material, MutableSet<String>> = HashMap()

    init {
        conditionType.convention(ConditionType.NONE)
        woolMaterials.convention(false)
    }

    fun wood(id: String, fungus: Boolean = false, config: MaterialDsl.() -> Unit = {}) {
        woodMaterials.add(WoodMaterial(Id.parse(id), fungus).also { config(MaterialDslImpl(it)) })
    }

    fun stone(id: String, brick: Boolean = false, config: MaterialDsl.() -> Unit = {}) {
        stoneMaterials.add(StoneMaterial(Id.parse(id), brick).also { config(MaterialDslImpl(it)) })
    }

    fun libcdConditions() {
        conditionType.set(ConditionType.LIBCD)
    }

    interface MaterialDsl {
        fun exclude(generator: String)
    }

    private inner class MaterialDslImpl(private val material: Material) : MaterialDsl {
        override fun exclude(generator: String) {
            exclusions.getOrPut(material, ::LinkedHashSet).add(generator)
        }
    }
}
