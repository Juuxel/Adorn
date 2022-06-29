package juuxel.adorn.util

import java.util.ArrayDeque
import java.util.Deque

class DupDeque<E>(private val deque: Deque<E> = ArrayDeque(), private val copier: (E) -> E) : Deque<E> by deque {
    fun duplicate() {
        push(copier(peek()))
    }
}
