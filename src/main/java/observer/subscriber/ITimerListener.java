package observer.subscriber;

public interface ITimerListener {
    default void register() {}
    default void remove() {}
    default void onTimerStart(int seconds) {}
    default void onTimerGet(int seconds) {}
    default void onTimerStop() {}
}

