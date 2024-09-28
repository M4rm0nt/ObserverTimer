package observer;

import observer.basisklassen.Console;
import observer.basisklassen.ConsoleLogger;
import observer.basisklassen.FileLogger;
import observer.basisklassen.ILogger;
import observer.publisher.ITimer;
import observer.publisher.Timer;
import observer.subscriber.FileLogCountdown;
import observer.subscriber.ITimerListener;
import observer.subscriber.LogCountdown;

// Steuerungsklasse
public class Main {

    private ITimer timer;

    public static void main(String[] args) {
        Main main = new Main();
        main.setupAndStartCountdown(2);
    }

    public void setupAndStartCountdown(int seconds) {

        this.timer = new Timer();

        ILogger logger = new ConsoleLogger(new Console());
        ITimerListener logCountdown = new LogCountdown(logger, this.timer);

        ILogger fileLogger = new FileLogger("src/main/resources/log");
        ITimerListener fileListener = new FileLogCountdown(fileLogger, this.timer);

        logCountdown.register();
        fileListener.register();
        this.timer.set(seconds);
        this.timer.start();

        this.timer.getCompletionFuture().thenRun(() -> {
            logCountdown.remove();
            fileListener.remove();
        });

    }

}
