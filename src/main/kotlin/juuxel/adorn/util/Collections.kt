package juuxel.adorn.util

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): EnumMap<K, V> =
    EnumMap<K, V>(K::class.java).apply { putAll(pairs) }
