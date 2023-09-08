package juuxel.adorn.datagen.gradle

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import java.io.File
import javax.inject.Inject

abstract class EmiDataGeneratorExtension {
    @get:Inject
    protected abstract val project: Project

    fun setupForPlatform(generatedResources: File? = null) {
        with(project) {
            tasks.named("generateEmi", GenerateEmi::class.java) {
                mustRunAfter(project(":common").tasks.named("generateData"))
                val resourceDirs = project(":common").sourceSets.getByName("main").resources.srcDirs +
                    sourceSets.getByName("main").resources.srcDirs
                for (dir in resourceDirs) {
                    // Ignore the AW resource dir
                    if (dir == generatedResources) continue

                    recipes.from(fileTree(dir) {
                        include("data/adorn/recipes/**")

                        // The unpacking recipes create "uncraftable" vanilla items like
                        // nether wart, so exclude them.
                        exclude("data/adorn/recipes/crates/unpack/**")
                    })
                }
            }
        }
    }

    companion object {
        private val Project.sourceSets: SourceSetContainer
            get() = extensions.getByType(JavaPluginExtension::class.java).sourceSets
    }
}
