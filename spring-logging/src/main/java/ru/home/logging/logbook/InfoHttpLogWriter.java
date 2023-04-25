package ru.home.logging.logbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Precorrelation;

import java.io.IOException;

/*
 * @created 21.04.2023
 * @author alexander
 */
public class InfoHttpLogWriter implements HttpLogWriter {

    private final Logger log = LoggerFactory.getLogger(Logbook.class);

    @Override
    public boolean isActive() {
        return log.isInfoEnabled();
    }

    @Override
    public void write(Precorrelation precorrelation, String request) throws IOException {
        log.info(request);
    }

    @Override
    public void write(Correlation correlation, String response) throws IOException {
        log.info(response);
    }
}