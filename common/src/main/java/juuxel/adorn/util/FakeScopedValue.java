package juuxel.adorn.util;

public final class FakeScopedValue<T> extends ThreadLocal<T> {
    public void with(T value, Runnable action) {
        T old = get();
        set(value);
        action.run();
        set(old);
    }
}
