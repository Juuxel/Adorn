package juuxel.adorn.util.animation;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractAnimatedProperty<T> {
    private final AnimationEngine engine;
    private final int duration;
    private final Interpolator<T> interpolator;
    private @Nullable Task currentTask = null;

    protected AbstractAnimatedProperty(AnimationEngine engine, int duration, Interpolator<T> interpolator) {
        this.engine = engine;
        this.duration = duration;
        this.interpolator = interpolator;
    }

    protected abstract void setRawValue(T value);

    public abstract T get();

    public synchronized void set(T value) {
        T oldValue = get();
        var oldTask = currentTask;
        if (oldTask != null) engine.remove(oldTask);

        if (!Objects.equals(oldValue, value)) {
            var task = new Task(oldValue, value);
            currentTask = task;
            engine.add(task);
        }
    }

    // https://easings.net/#easeOutQuint
    private static float ease(float delta) {
        return 1 - (float) Math.pow(1 - delta, 5);
    }

    private final class Task implements AnimationTask {
        private final T from;
        private final T to;
        private int age = 0;

        private Task(T from, T to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean isAlive() {
            return age < duration;
        }

        @Override
        public void tick() {
            age++;
            float delta = ease((float) age / (float) duration);
            T newValue = interpolator.interpolate(delta, from, to);
            setRawValue(newValue);
        }

        @Override
        public void removed() {
            synchronized (AbstractAnimatedProperty.this) {
                if (currentTask == this) {
                    currentTask = null;
                }
            }
        }
    }
}
