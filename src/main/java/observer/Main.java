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

        ILogger consoleLogger = new ConsoleLogger(new Console());
        ITimerListener logCountdown = new LogCountdown(consoleLogger, this.timer);

        try (FileLogger fileLogger = new FileLogger("src/main/resources/log")) {
            ITimerListener fileLogCountdown = new FileLogCountdown(fileLogger, this.timer);

            logCountdown.register();
            fileLogCountdown.register();

            this.timer.set(seconds);
            this.timer.start();

            this.timer.getCompletionFuture().join();

            logCountdown.remove();
            fileLogCountdown.remove();

        } catch (Exception e) {
            System.err.println("Fehler beim Ausführen des Countdowns: " + e.getMessage());
        }
    }
}
