package juuxel.adorn.util.animation

interface AnimationTask {
    fun isAlive(): Boolean
    fun tick()
    fun removed() {}
}
