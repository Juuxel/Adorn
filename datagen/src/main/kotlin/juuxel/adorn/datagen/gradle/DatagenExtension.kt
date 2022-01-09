package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.WoodMaterial
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class DatagenExtension {
    abstract val woodMaterials: SetProperty<WoodMaterial>
    abstract val stoneMaterials: SetProperty<StoneMaterial>
    abstract val conditionType: Property<ConditionType>
    abstract val woolMaterials: Property<Boolean>

    init {
        conditionType.convention(ConditionType.NONE)
        woolMaterials.convention(false)
    }

    fun wood(id: String, fungus: Boolean = false) {
        woodMaterials.add(WoodMaterial(Id.parse(id), fungus))
    }

    fun stone(id: String, brick: Boolean = false) {
        stoneMaterials.add(StoneMaterial(Id.parse(id), brick))
    }

    fun libcdConditions() {
        conditionType.set(ConditionType.LIBCD)
    }
}
