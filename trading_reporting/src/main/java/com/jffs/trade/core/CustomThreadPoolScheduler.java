package com.jffs.trade.core;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolScheduler {
    private final ScheduledExecutorService scheduler;

    public CustomThreadPoolScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleRunOnce(Runnable task, Instant timeToRun) {
        final var delay = Duration.between(Instant.now(), timeToRun);
        scheduler.schedule(task, delay.getNano(), TimeUnit.NANOSECONDS);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant initialDelay, int intervalInMs, TimeUnit timeUnit) {
        final var delay = Duration.between(Instant.now(), initialDelay).toMillis();
        return scheduler.scheduleAtFixedRate(task, delay, intervalInMs, timeUnit);
    }

    public void close() {
        scheduler.shutdown();
        scheduler.shutdownNow();
    }
}
