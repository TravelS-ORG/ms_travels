package com.group.ms_travels.core.audit.log;

import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

// Using when argument over-consume CPU (serialize, join, hash, lazy load)
public class LazyLogUtil {

    private LazyLogUtil() {}

    public static void info(Logger logger, String msg, Supplier<List<Object>> args) {
        if (logger.isInfoEnabled()) logger.info(msg, args.get().toArray());
    }

    public static void debug(Logger logger, String msg, Supplier<List<Object>> args) {
        if (logger.isDebugEnabled()) logger.debug(msg, args.get().toArray());
    }

    public static void warn(Logger logger, String msg, Supplier<List<Object>> args) {
        if (logger.isWarnEnabled()) logger.warn(msg, args.get().toArray());
    }

    public static void error(Logger logger, String msg, Supplier<List<Object>> args) {
        if (logger.isErrorEnabled()) logger.error(msg, args.get().toArray());
    }

}
