package utils;

import org.apache.log4j.Logger;

public class LoggerUtil {

    public static Logger logger = Logger.getLogger("EaseMyTripLogger");

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
}