package com.jffs.trade;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;

public class GracefulShutdownHook implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("APP");

    private static final String GRACEFUL_SHUTDOWN_WAIT_SECONDS = "GracefulShutdownWaitSeconds";

    private static final String DEFAULT_GRACEFUL_SHUTDOWN_WAIT_SECONDS = "20";

    private final ConfigurableApplicationContext applicationContext;

    public GracefulShutdownHook(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void run() {
        delayShutdownSpringContext();
        shutdownSpringContext();
    }

    private void delayShutdownSpringContext() {
        try {
            final int shutdownWaitSeconds = getShutdownWaitSeconds();
            LOGGER.info("Going to wait for {} seconds before shutdown SpringContext!", shutdownWaitSeconds);
            Thread.sleep(shutdownWaitSeconds * 1000L);
        } catch (InterruptedException e) {
            LOGGER.error("Error while graceful shutdown Thread.sleep", e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }

    private void shutdownSpringContext() {
        LOGGER.info("Spring Application context starting to shutdown");
        applicationContext.close();
        LOGGER.info("Spring Application context is shutdown");
    }

    private int getShutdownWaitSeconds() {
        String waitSeconds = System.getProperty(GRACEFUL_SHUTDOWN_WAIT_SECONDS);
        if (StringUtils.isEmpty(waitSeconds)) {
            waitSeconds = applicationContext.getEnvironment().getProperty(GRACEFUL_SHUTDOWN_WAIT_SECONDS,
                    DEFAULT_GRACEFUL_SHUTDOWN_WAIT_SECONDS);
        }
        return Integer.parseInt(waitSeconds);
    }
}
