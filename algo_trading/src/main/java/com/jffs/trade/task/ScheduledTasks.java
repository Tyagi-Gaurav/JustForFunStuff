package com.jffs.trade.task;

import com.jffs.trade.oanda.OandaClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ScheduledTasks {
    private static final Logger LOG = LogManager.getLogger("APP");
    private final OandaClient oandaClient;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public ScheduledTasks(OandaClient oandaClient,
                          ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.oandaClient = oandaClient;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        threadPoolTaskScheduler.schedule(this::getHistoricalData, Instant.now().plus(5, ChronoUnit.SECONDS));
    }

    public void getHistoricalData() {
        LOG.info(oandaClient.getHistoricalData("EUR_USD"));
        if (threadPoolTaskScheduler.isRunning()) {
            threadPoolTaskScheduler.schedule(this::getHistoricalData, Instant.now().plus(5, ChronoUnit.SECONDS));
        }
    }
}
