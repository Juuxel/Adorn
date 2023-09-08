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
        // Null check to make sure that this function can be called in Screen.init.
        // It's also called when the screen is resized, so creating a new thread each time
        //   1. leaks animator threads
        //   2. causes the animations to speed up unreasonably
        if (thread == null) {
            thread = AnimatorThread().also { it.start() }
        }
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
