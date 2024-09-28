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
        main.setupAndStartCountdown(2);
    }

    public void setupAndStartCountdown(int seconds) {
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
            System.out.println(timer.getListeners().size() + " Listener registriert.");

            this.timer.set(seconds);
            this.timer.start();

            this.timer.getCompletionFuture().join();

            logCountdown.remove();
            fileLogCountdown.remove();

            System.out.println(timer.getListeners().size() + " Listener registriert.");

        } catch (Exception e) {
            System.err.println("Fehler beim Ausführen des Countdowns: " + e.getMessage());
        }
    }
}
