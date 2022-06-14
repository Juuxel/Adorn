package juuxel.adorn.datagen.gradle

import org.gradle.api.provider.Property

abstract class DataGeneratorExtension {
    abstract val conditionType: Property<String>

    init {
        conditionType.convention("none")
    }
}
