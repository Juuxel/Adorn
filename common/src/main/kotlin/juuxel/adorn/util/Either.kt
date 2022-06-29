package juuxel.adorn.util

sealed interface Either<out L, out R>
data class Left<out L>(val value: L) : Either<L, Nothing>
data class Right<out R>(val value: R) : Either<Nothing, R>
