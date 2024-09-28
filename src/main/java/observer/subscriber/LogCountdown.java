package observer.subscriber;

import observer.basisklassen.ILogger;
import observer.publisher.ITimer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogCountdown implements ITimerListener {

    private final ILogger logger;
    private final ITimer timer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogCountdown(ILogger logger, ITimer timer) {
        this.logger = logger;
        this.timer = timer;
    }

    public void register() {
        this.timer.addListener(this);
    }

    public void remove() {
        this.timer.removeListener(this);
    }

    @Override
    public void onTimerStart(int sekunden) {
        String zeit = LocalTime.now().format(this.formatter);
        this.logger.info("Countdown von %d Sekunden gestartet um %s.", sekunden, zeit);
    }

    @Override
    public void onTimerGet(int sekunden) {
        this.logger.info(String.valueOf(sekunden));
    }

    @Override
    public void onTimerStop() {
        String zeit = LocalTime.now().format(this.formatter);
        this.logger.info("Countdown gestoppt um %s.", zeit);
    }

}