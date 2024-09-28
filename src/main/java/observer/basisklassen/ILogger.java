package observer.basisklassen;

public interface ILogger {
    void info(String message);
    void info(String format, Object... args);
    void error(String message);
    void error(String format, Object... args);
    void debug(String message);
    void debug(String format, Object... args);
}
