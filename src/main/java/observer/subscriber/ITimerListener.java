package observer.subscriber;

import observer.publisher.ITimer;

public interface ITimerListener {
    default void setPublisher(ITimer publisher) {}
    default void register() {}
    default void remove() {}
    default void onTimerStart(int seconds) {}
    default void onTimerGet(int seconds) {}
    default void onTimerStop() {}
}

