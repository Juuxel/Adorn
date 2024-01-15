package juuxel.adorn.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)

/**
 * Gets a logger for the calling class.
 *
 * If called from a nested class, returns a logger
 * for the outermost class in the nest.
 *
 * If called from a top-level property initialiser,
 * returns a logger for the enclosing file.
 */
fun logger(): Logger {
    var caller = STACK_WALKER.getCallerClass()

    // Locate the outermost class.
    var next = caller.enclosingClass
    while (next != null) {
        caller = next
        next = caller.enclosingClass
    }

    return LoggerFactory.getLogger(caller)
}
