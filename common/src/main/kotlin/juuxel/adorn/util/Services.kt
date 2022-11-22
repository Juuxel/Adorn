package juuxel.adorn.util

import java.util.Optional
import java.util.ServiceLoader

/**
 * Marks `ServiceLoader.load(A::class.java).findFirst()` for inlining in the target class.
 * For companion property delegates, should be on the containing class for classes and
 * on the companion for interfaces.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class InlineServices

inline fun <reified T> loadService(): T =
    ServiceLoader.load(T::class.java).findFirst().unwrapService(T::class.java)

fun <T> Optional<T>.unwrapService(type: Class<T>): T =
    orElseThrow { RuntimeException("Could not find Adorn platform service ${type.simpleName}") }
