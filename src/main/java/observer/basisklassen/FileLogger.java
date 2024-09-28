package observer.basisklassen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements ILogger {

    private final String filePath;
    private PrintWriter writer;

    public FileLogger(String filePath) {
        this.filePath = filePath + ".txt";

        try {
            this.writer = new PrintWriter(new FileWriter(this.filePath, false));
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Öffnen der Log-Datei: " + e.getMessage(), e);
        }
    }

    @Override
    public void info(String message) {
        writer.println("INFO: " + message);
        writer.flush();
    }

    @Override
    public void info(String format, Object... args) {
        String message = String.format(format, args);
        info(message);
    }

    @Override
    public void error(String message) {
        writer.println("ERROR: " + message);
        writer.flush();
    }

}
