package observer;

import observer.basisklassen.Console;
import observer.basisklassen.ConsoleLogger;
import observer.basisklassen.FileLogger;
import observer.basisklassen.ILogger;
import observer.publisher.ITimer;
import observer.publisher.Timer;
import observer.subscriber.FileLogCountdown;
import observer.subscriber.ITimerListener;
import observer.subscriber.ConsoleLogCountdown;

// Steuerungsklasse
public class Main {

    private ITimer timer;

    public static void main(String[] args) {
        Main main = new Main();
        main.setupAndStartCountdown(4);
    }

    public void setupAndStartCountdown(int seconds) {

        if (seconds <= 0) {
            System.out.println("Bitte geben Sie eine Zahl größer als 0 ein.");
            return;
        }

        this.timer = new Timer();

        ILogger consoleLogger = new ConsoleLogger(new Console());
        ITimerListener logCountdown = new ConsoleLogCountdown(consoleLogger);
        logCountdown.setPublisher(this.timer);

        try (FileLogger fileLogger = new FileLogger("src/main/resources/log")) {
            ITimerListener fileLogCountdown = new FileLogCountdown(fileLogger);
            fileLogCountdown.setPublisher(this.timer);

            logCountdown.register();
            fileLogCountdown.register();

            Timer timer = (Timer) this.timer;
            consoleLogger.debug("%d " + "Listener registriert.", timer.getListeners().size());

            this.timer.set(seconds);
            this.timer.start();

            this.timer.getCompletionFuture().join();

            logCountdown.remove();
            fileLogCountdown.remove();

            consoleLogger.debug("%d " + "Listener registriert.", timer.getListeners().size());

        } catch (Exception e) {
            consoleLogger.error("Fehler beim Ausführen des Countdowns: %s", e.getMessage());
        }
    }
}
