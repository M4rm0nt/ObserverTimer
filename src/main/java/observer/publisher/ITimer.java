package observer.publisher;

import observer.subscriber.ITimerListener;

import java.util.concurrent.CompletableFuture;

public interface ITimer {
    void start();
    void stop();
    void set(int seconds);
    void addListener(ITimerListener listener);
    void removeListener(ITimerListener listener);
    CompletableFuture<Void> getCompletionFuture();
}
