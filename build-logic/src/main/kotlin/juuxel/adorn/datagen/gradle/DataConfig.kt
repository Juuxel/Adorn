package juuxel.adorn.datagen.gradle

import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class DataConfig @Inject constructor(private val name: String) : Named {
    abstract val files: ConfigurableFileCollection
    abstract val tagsOnly: Property<Boolean>

    init {
        tagsOnly.convention(false)
    }

    override fun getName(): String = name
}
