package observer.publisher;

import observer.subscriber.ITimerListener;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Timer implements ITimer {

    private final CompletableFuture<Void> completionFuture = new CompletableFuture<>();
    private final CopyOnWriteArrayList<ITimerListener> listeners = new CopyOnWriteArrayList<>();
    private ScheduledExecutorService scheduler;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicInteger seconds = new AtomicInteger();

    @Override
    public CompletableFuture<Void> getCompletionFuture() {
        return this.completionFuture;
    }

    @Override
    public void addListener(ITimerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ITimerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void set(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Die Anzahl der Sekunden muss größer als 0 sein.");
        }
        this.seconds.set(seconds);
    }

    @Override
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            if (scheduler == null || scheduler.isShutdown()) {
                scheduler = Executors.newSingleThreadScheduledExecutor();
            }
            notifyTimerStart();
            scheduler.scheduleAtFixedRate(this::timerTick, 0, 1, TimeUnit.SECONDS);
        } else {
            notifyTimerAlreadyRunning();
        }
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

    private void notifyTimerAlreadyRunning() {
        notifyListeners(ITimerListener::onTimerAlreadyRunning);
    }

    @Override
    public void stop() {
        if (!this.isRunning.compareAndSet(true, false)) return;

        notifyTimerStop();
        shutdownScheduler();
        completionFuture.complete(null);
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
            try {
                action.accept(listener);
            } catch (Exception e) {
                System.err.format("Fehler beim Benachrichtigen des Listeners %s: %s%n", listener, e.getMessage());
            }
        }
    }

}
