package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.transform.TransformDsl
import juuxel.adorn.datagen.transform.TransformSet
import org.gradle.api.Action

open class DataGenerationExtension {
    internal val transformSets: MutableMap<String, MutableList<TransformSet>> = HashMap()

    fun transformJson(filePattern: String, action: Action<TransformDsl>) {
        val transformSet = TransformSet.Builder().also { action.execute(it) }.build()
        transformSets.getOrPut(filePattern, ::ArrayList) += transformSet
    }
}
