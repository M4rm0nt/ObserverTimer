package observer;

import observer.publisher.ITimer;
import observer.subscriber.ITimerListener;

public interface ICountdown {
    void start(int sekunden, ITimerListener iTimerListener, ITimer timer);
}
