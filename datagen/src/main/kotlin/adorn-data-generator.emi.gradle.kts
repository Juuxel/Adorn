import juuxel.adorn.datagen.gradle.GenerateData
import juuxel.adorn.datagen.gradle.GenerateEmi

val generateEmi by tasks.registering(GenerateEmi::class) {
    val generateMainData = tasks.named("generateMainData", GenerateData::class)
    mustRunAfter(generateMainData)
    output.convention(generateMainData.flatMap { it.output })
}

tasks.named("generateData") {
    dependsOn(generateEmi)
}
