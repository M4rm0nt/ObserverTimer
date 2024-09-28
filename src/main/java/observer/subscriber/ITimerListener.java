package observer.subscriber;

public interface ITimerListener {
    void register();
    void remove();
    default void onTimerStart(int seconds) {}
    default void onTimerGet(int seconds) {}
    default void onTimerStop() {}
}

