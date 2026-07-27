package juuxel.adorn.util.animation;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AnimationEngine {
    private final List<AnimationTask> tasks = new ArrayList<>();
    private @Nullable AnimatorThread thread = null;

    public void add(AnimationTask task) {
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    public void remove(AnimationTask task) {
        synchronized (tasks) {
            tasks.remove(task);
        }
    }

    public void start() {
        // Null check to make sure that this function can be called in Screen.init.
        // It's also called when the screen is resized, so creating a new thread each time
        //   1. leaks animator threads
        //   2. causes the animations to speed up unreasonably
        if (thread == null) {
            var thread = new AnimatorThread();
            thread.start();
            this.thread = thread;
        }
    }

    public void stop() {
        var current = thread;
        if (current != null) current.interrupt();
        thread = null;
    }

    private final class AnimatorThread extends Thread {
        private AnimatorThread() {
            super("Adorn animator");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!interrupted()) {
                synchronized (tasks) {
                    var iter = tasks.iterator();
                    while (iter.hasNext()) {
                        var task = iter.next();
                        if (task.isAlive()) {
                            task.tick();
                        } else {
                            task.removed();
                            iter.remove();
                        }
                    }
                }

                try {
                    // noinspection BusyWait
                    sleep(10L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
