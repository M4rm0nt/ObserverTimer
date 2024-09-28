package observer.basisklassen;

/**
 * Logger, der Nachrichten auf der Konsole ausgibt.
 */
public class ConsoleLogger implements ILogger {

    private final IConsole console;

    public ConsoleLogger(IConsole console) {
        this.console = console;
    }

    @Override
    public void info(String format, Object... args) {
        String message = String.format(format, args);
        info(message);
    }

    @Override
    public void info(String message) {
        console.print("INFO: " + message);
    }

    @Override
    public void error(String message) {
        console.print("ERROR: " + message);
    }
}
