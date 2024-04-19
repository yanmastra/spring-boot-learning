package com.yanmastra.msSecurityBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class Log {
    public static final ColorLog log = new ColorLog(LoggerFactory.getLogger(Log.class));

    public static final class ColorLog {
        private final Logger logger;

        private ColorLog(Logger logger) {
            this.logger = logger;
        }

        public void debug(String var1, Object var2, Object var3) {
            this.debug(var1, new Object[]{var2, var3});
        }
        public void debug(String var1, Object... var2) {
            logger.debug("\u001B[34m" + var1 + "\u001B[0m", var2);
        }

        public void debug(String var1, Throwable var2) {
            logger.debug("\u001B[34m" + var1 + "\u001B[0m \u001B[31m" + extractTrace(var2) + "\u001B[0m");
        }

        public void info(String var1, Object var2, Object var3) {
            this.info(var1, new Object[]{var2, var3});
        }
        public void info(String var1, Object... var2) {
            logger.info("\u001B[32m" + var1 + "\u001B[0m", var2);
        }

        public void info(String var1, Throwable var2) {
            logger.debug("\u001B[32m" + var1 + "\u001B[0m \u001B[31m" + extractTrace(var2) + "\u001B[0m");
        }

        public void error(String var1, Object var2, Object var3) {
            this.error(var1, new Object[]{var2, var3});
        }
        public void error(String var1, Object... var2) {
            logger.error("\u001B[31m" + var1 + "\u001B[0m", var2);
        }

        public void error(String var1, Throwable var2) {
            logger.error("\u001B[31m" + var1 + extractTrace(var2) +  "\u001B[0m");
        }

        public void warn(String var1, Object var2, Object var3) {
            this.warn(var1, new Object[]{var2, var3});
        }
        public void warn(String var1, Object... var2) {
            logger.warn("\u001B[30m" + var1 + "\u001B[0m", var2);
        }

        public void warn(String var1, Throwable var2) {
            logger.warn("\u001B[30m" + var1 + extractTrace(var2) + "\u001B[0m");
        }

        private String extractTrace(Throwable throwable) {
            StringBuilder sbError = new StringBuilder("\n\n\t");
            if (throwable == null) {
                sbError.append("null");
            } else {
                sbError.append(throwable);
                Stream.of(throwable.getStackTrace()).map(StackTraceElement::toString)
                        .forEach(s -> sbError.append("\n\t\t").append(s));

                if (throwable.getCause() != null && !throwable.equals(throwable.getCause())) {
                    sbError.append(extractTrace(throwable.getCause()));
                }
            }
            return  sbError.toString();
        }
    }
}
