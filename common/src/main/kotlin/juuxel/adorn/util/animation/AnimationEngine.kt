package juuxel.adorn.util.animation

class AnimationEngine {
    private val tasks: MutableList<AnimationTask> = ArrayList()
    private var thread: AnimatorThread? = null

    fun add(task: AnimationTask) {
        synchronized(tasks) {
            tasks += task
        }
    }

    fun remove(task: AnimationTask) {
        synchronized(tasks) {
            tasks -= task
        }
    }

    fun start() {
        thread = AnimatorThread().also { it.start() }
    }

    fun stop() {
        thread?.interrupt()
        thread = null
    }

    private inner class AnimatorThread : Thread("Adorn animator") {
        override fun run() {
            while (!interrupted()) {
                synchronized(tasks) {
                    val iter = tasks.iterator()
                    while (iter.hasNext()) {
                        val task = iter.next()
                        if (task.isAlive()) {
                            task.tick()
                        } else {
                            task.removed()
                            iter.remove()
                        }
                    }
                }

                try {
                    sleep(10L)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
    }
}
