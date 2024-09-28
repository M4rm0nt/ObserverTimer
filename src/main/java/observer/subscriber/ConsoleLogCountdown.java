package observer.subscriber;

import observer.basisklassen.ILogger;

public class ConsoleLogCountdown extends AbstractLog {

    public ConsoleLogCountdown(ILogger logger) {
        super(logger);
    }
}
