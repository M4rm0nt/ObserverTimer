package observer.basisklassen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements ILogger, AutoCloseable {
    private final String filePath;
    private final PrintWriter writer;

    public FileLogger(String filePath) throws IOException {
        this.filePath = filePath + ".txt";
        this.writer = new PrintWriter(new FileWriter(this.filePath, false));
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void info(String format, Object... args) {
        String message = String.format(format, args);
        info(message);
    }

    @Override
    public void info(String message) {
        writer.println("INFO: " + message);
        writer.flush();
    }

    @Override
    public void error(String format, Object... args) {
        String message = String.format(format, args);
        error(message);
    }

    @Override
    public void error(String message) {
        writer.println("ERROR: " + message);
        writer.flush();
    }

    @Override
    public void debug(String format, Object... args) {
        String message = String.format(format, args);
        debug(message);
    }

    @Override
    public void debug(String message) {
        writer.println("DEBUG: " + message);
        writer.flush();
    }
}
