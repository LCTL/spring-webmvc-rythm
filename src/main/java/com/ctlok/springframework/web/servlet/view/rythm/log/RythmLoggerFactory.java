package com.ctlok.springframework.web.servlet.view.rythm.log;

import com.greenlaw110.rythm.extension.ILoggerFactory;
import com.greenlaw110.rythm.logger.ILogger;
import org.slf4j.LoggerFactory;

/**
 * @author Lawrence Cheung
 */
public class RythmLoggerFactory implements ILoggerFactory {

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new RythmSlf4jLogger(LoggerFactory.getLogger(clazz));
    }

}
