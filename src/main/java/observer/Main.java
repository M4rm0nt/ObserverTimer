package observer;

import observer.basisklassen.Console;
import observer.basisklassen.ConsoleLogger;
import observer.basisklassen.ILogger;
import observer.publisher.ITimer;
import observer.publisher.Timer;
import observer.subscriber.ITimerListener;
import observer.subscriber.LogCountdown;

// Steuerungsklasse
public class Main implements ICountdown {

    private ITimer timer;

    public static void main(String[] args) {
        Main main = new Main();
        main.setupAndStartCountdown(10);
    }

    public void setupAndStartCountdown(int seconds) {

        this.timer = new Timer();

        ILogger logger = new ConsoleLogger(new Console());
        ITimerListener iTimerListener = new LogCountdown(logger, this.timer);

        start(seconds, iTimerListener,this.timer);

        this.timer.getCompletionFuture().thenRun(() -> {
            iTimerListener.remove();
        });

    }

    @Override
    public void start(int seconds, ITimerListener iTimerListener, ITimer timer) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Die Anzahl der Sekunden muss größer als 0 sein.");
        }
        iTimerListener.register();
        this.timer.set(seconds);
        this.timer.start();
    }
}
