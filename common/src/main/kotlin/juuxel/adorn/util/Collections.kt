package juuxel.adorn.util

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): EnumMap<K, V> =
    EnumMap<K, V>(K::class.java).apply { putAll(pairs) }

/**
 * Interleaves elements of all [lists].
 *
 * For example, if this method is passed the list `[[a, b, c], [A, B, C, D]]`,
 * the expected output is `[a, A, b, B, c, C, D]`.
 */
fun <E> interleave(lists: List<List<E>>): List<E> {
    val size = lists.sumOf { it.size }
    val maxSize = lists.maxOf { it.size }
    val output = ArrayList<E>(size)

    for (i in 0 until maxSize) {
        for (list in lists) {
            if (i in list.indices) {
                output += list[i]
            }
        }
    }

    return output
}
