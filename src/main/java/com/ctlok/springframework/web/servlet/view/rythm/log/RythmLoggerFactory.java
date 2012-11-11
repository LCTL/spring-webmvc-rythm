package com.ctlok.springframework.web.servlet.view.rythm.log;

import org.slf4j.LoggerFactory;

import com.greenlaw110.rythm.logger.ILogger;
import com.greenlaw110.rythm.logger.ILoggerFactory;

/**
 * @author Lawrence Cheung
 *
 */
public class RythmLoggerFactory implements ILoggerFactory {

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new RythmSlf4jLogger(LoggerFactory.getLogger(clazz));
    }

}
