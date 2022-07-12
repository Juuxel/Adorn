import juuxel.adorn.datagen.gradle.DataGenerationExtension
import juuxel.adorn.datagen.gradle.DeleteDuplicates
import juuxel.adorn.datagen.gradle.GenerateData
import juuxel.adorn.datagen.gradle.GenerateTags
import juuxel.adorn.datagen.gradle.TransformJson
import juuxel.adorn.datagen.util.MinifyJson

plugins {
    java
}

val extension = extensions.create("dataGeneration", DataGenerationExtension::class.java)

val generatedResources = layout.projectDirectory.dir("src/main/generatedResources")

val generateMainData by tasks.registering(GenerateData::class) {
    configs.from(fileTree("src/data").filter { it.extension == "xml" })
    output.convention(generatedResources)
}

val generateTags by tasks.registering(GenerateTags::class) {
    dependsOn(generateMainData)
    output.convention(generatedResources)
}

val generateData by tasks.registering {
    dependsOn(generateMainData, generateTags)
}

val deleteDuplicateResources by tasks.registering(DeleteDuplicates::class) {
    generated.convention(generatedResources)
    main.convention(layout.dir(sourceSets.main.map { it.resources.srcDirs.first() }))
    mustRunAfter(generateMainData, generateTags, generateData)
}

val processResources = tasks.processResources

val transformJson by tasks.registering(TransformJson::class) {
    transformSets.convention(project.provider { extension.transformSets })
    directory.convention(layout.dir(processResources.map { it.destinationDir }))
}

processResources {
    finalizedBy(transformJson)
}

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}

afterEvaluate {
    tasks.named("remapJar") {
        doLast(MinifyJson)
    }
}
