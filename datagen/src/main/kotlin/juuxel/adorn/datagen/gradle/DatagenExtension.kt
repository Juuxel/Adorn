package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.TemplateDsl
import juuxel.adorn.datagen.WoodMaterial
import juuxel.adorn.datagen.buildSubstitutions
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class DatagenExtension {
    abstract val woodMaterials: SetProperty<WoodMaterial>
    abstract val stoneMaterials: SetProperty<StoneMaterial>
    abstract val conditionType: Property<ConditionType>
    abstract val colorMaterials: Property<Boolean>
    internal val exclusions: MutableMap<Id, MutableSet<String>> = HashMap()
    abstract val extraProperties: MapProperty<Id, Map<String, String>>

    init {
        conditionType.convention(ConditionType.NONE)
        colorMaterials.convention(false)
    }

    fun wood(id: String, fungus: Boolean = false, config: MaterialDsl.() -> Unit = {}) {
        woodMaterials.add(WoodMaterial(Id.parse(id), fungus).also { config(MaterialDslImpl(it)) })
    }

    fun stone(id: String, brick: Boolean = false, hasSidedTexture: Boolean = false, config: MaterialDsl.() -> Unit = {}) {
        stoneMaterials.add(StoneMaterial(Id.parse(id), brick, hasSidedTexture).also { config(MaterialDslImpl(it)) })
    }

    fun libcdConditions() {
        conditionType.set(ConditionType.LIBCD)
    }

    fun fabricConditions() {
        conditionType.set(ConditionType.FABRIC)
    }

    fun forgeConditions() {
        conditionType.set(ConditionType.FORGE)
    }

    interface MaterialDsl {
        fun exclude(generator: String)
        fun replace(block: TemplateDsl.() -> Unit)
    }

    private inner class MaterialDslImpl(private val material: Material) : MaterialDsl {
        override fun exclude(generator: String) {
            exclusions.getOrPut(material.id, ::LinkedHashSet).add(generator)
        }

        override fun replace(block: TemplateDsl.() -> Unit) {
            extraProperties.put(material.id, buildSubstitutions(block))
        }
    }
}
