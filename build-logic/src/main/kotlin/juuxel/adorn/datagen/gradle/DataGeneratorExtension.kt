@file:Suppress("LeakingThis")

package juuxel.adorn.datagen.gradle

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class DataGeneratorExtension @Inject constructor(project: Project) {
    abstract val generateTags: Property<Boolean>

    val configs: NamedDomainObjectContainer<DataConfig> = project.container(DataConfig::class.java) { name ->
        val dataConfig = project.objects.newInstance(DataConfig::class.java, name)
        dataConfig.scope.set(DataScope.ALL)
        dataConfig
    }

    init {
        generateTags.convention(false)
    }

    fun configs(action: Action<NamedDomainObjectContainer<DataConfig>>) {
        action.execute(configs)
    }
}
