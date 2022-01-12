import juuxel.adorn.datagen.ColorMaterial
import juuxel.adorn.datagen.gradle.DatagenExtension
import juuxel.adorn.datagen.gradle.DeleteDuplicates
import juuxel.adorn.datagen.gradle.GenerateData

plugins {
    java
}

val extension: DatagenExtension = extensions.create("datagen")
val generatedResources = layout.projectDirectory.dir("src/main/generatedResources")

val generateData by tasks.registering(GenerateData::class) {
    woodMaterials.convention(extension.woodMaterials)
    stoneMaterials.convention(extension.stoneMaterials)
    colorMaterials.convention(extension.colorMaterials.map { if (it) ColorMaterial.values().toList() else emptyList() })
    output.convention(generatedResources)
    conditionType.convention(extension.conditionType)
    exclusions.convention(extension.exclusions)
    extraProperties.convention(extension.extraProperties)
}

val deleteDuplicateResources by tasks.registering(DeleteDuplicates::class) {
    generated.convention(generatedResources)
    main.convention(layout.dir(sourceSets.main.map { it.resources.srcDirs.first() }))
    mustRunAfter(generateData)
}

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}
