package observer.subscriber;

import observer.basisklassen.ILogger;

public class FileLogCountdown extends AbstractLog {

    public FileLogCountdown(ILogger logger) {
        super(logger);
    }
}
