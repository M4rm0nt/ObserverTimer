package observer.publisher;

import observer.subscriber.ITimerListener;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Timer implements ITimer {

    public Timer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownScheduler));
    }

    private final CompletableFuture<Void> completionFuture = new CompletableFuture<>();
    private final CopyOnWriteArrayList<ITimerListener> listeners = new CopyOnWriteArrayList<>();
    private ScheduledExecutorService scheduler;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicInteger seconds = new AtomicInteger();

    @Override
    public void addListener(ITimerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ITimerListener listener) {
        this.listeners.removeIf(Objects::isNull);
        this.listeners.remove(listener);
    }

    @Override
    public void set(int seconds) {
        if (seconds > 0) {
            this.seconds.set(seconds);
        }
    }

    @Override
    public void start() {
        if (this.isRunning.compareAndSet(false, true)) {
            if (this.scheduler == null || this.scheduler.isShutdown()) {
                this.scheduler = Executors.newSingleThreadScheduledExecutor();
            }
            notifyTimerStart();
            this.scheduler.scheduleAtFixedRate(this::timerTick, 0, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public CompletableFuture<Void> getCompletionFuture() {
        return this.completionFuture;
    }

    private void timerTick() {
        int currentSeconds = this.seconds.get();

        if (currentSeconds <= 0) {
            stop();
            return;
        }

        notifyTimerGet();
        this.seconds.decrementAndGet();
    }

    @Override
    public void stop() {
        if (this.isRunning.compareAndSet(true, false)) {
            notifyTimerStop();
            shutdownScheduler();
            this.completionFuture.complete(null);
        }
    }


    private void shutdownScheduler() {
        if (this.scheduler != null && !this.scheduler.isShutdown()) {
            this.scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    this.scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                this.scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            } finally {
                this.scheduler = null;
            }
        }
    }

    private void notifyTimerStart() {
        notifyListeners(listener -> listener.onTimerStart(this.seconds.get()));
    }

    private void notifyTimerGet() {
        notifyListeners(listener -> listener.onTimerGet(this.seconds.get()));
    }

    private void notifyTimerStop() {
        notifyListeners(ITimerListener::onTimerStop);
    }

    private void notifyListeners(Consumer<ITimerListener> action) {
        for (ITimerListener listener : this.listeners) {
                action.accept(listener);
        }
    }

    public CopyOnWriteArrayList<ITimerListener> getListeners() {
        return listeners;
    }
}