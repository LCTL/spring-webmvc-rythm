package com.ctlok.springframework.web.servlet.view.rythm.log;

import org.rythmengine.logger.ILogger;
import org.slf4j.Logger;

/**
 * @author Lawrence Cheung
 */
public class RythmSlf4jLogger implements ILogger {

    private final Logger logger;

    public RythmSlf4jLogger(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String message, Object... args) {
        if (this.isDebugEnabled()) {
            logger.debug(this.format(message, args));
        }
    }

    @Override
    public void debug(Throwable throwable, String message, Object... args) {
        if (this.isDebugEnabled()) {
            logger.debug(this.format(message, args), throwable);
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (this.isErrorEnabled()) {
            logger.error(this.format(message, args));
        }
    }

    @Override
    public void error(Throwable throwable, String message, Object... args) {
        if (this.isErrorEnabled()) {
            logger.error(this.format(message, args), throwable);
        }
    }

    @Override
    public void info(String message, Object... args) {
        if (this.isInfoEnabled()) {
            logger.info(this.format(message, args));
        }
    }

    @Override
    public void info(Throwable throwable, String message, Object... args) {
        if (this.isInfoEnabled()) {
            logger.error(this.format(message, args), throwable);
        }
    }

    @Override
    public void trace(String message, Object... args) {
        if (this.isTraceEnabled()) {
            logger.trace(this.format(message, args));
        }
    }

    @Override
    public void trace(Throwable throwable, String message, Object... args) {
        if (this.isTraceEnabled()) {
            logger.error(this.format(message, args), throwable);
        }
    }

    @Override
    public void warn(String message, Object... args) {
        if (this.isWarnEnabled()) {
            logger.warn(this.format(message, args));
        }
    }

    @Override
    public void warn(Throwable throwable, String message, Object... args) {
        if (this.isWarnEnabled()) {
            logger.error(this.format(message, args), throwable);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    private String format(String message, Object... args) {
        return String.format(message, args);
    }

}
