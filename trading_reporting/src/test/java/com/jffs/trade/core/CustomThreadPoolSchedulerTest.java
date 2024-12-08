package com.jffs.trade.core;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jffs.trade.TestUtils.millisElapsedSince;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomThreadPoolSchedulerTest {
    private static final long ASSERTION_TIMEOUT_MS = 2000;
    CustomThreadPoolScheduler underTest = new CustomThreadPoolScheduler(
            Executors.newScheduledThreadPool(1));

    @Test
    void scheduleRunOnceATaskNow() throws InterruptedException {
        final var atomicInteger = new AtomicInteger(0);
        final var countDownLatch = new CountDownLatch(1);
        underTest.scheduleRunOnce(() -> {
            atomicInteger.incrementAndGet();
            countDownLatch.countDown();
        }, Instant.now());

        countDownLatch.await(ASSERTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        assertThat(atomicInteger.get()).isOne();
    }

    @Test
    void scheduleRunOnceATasWithDelay() throws InterruptedException {
        final var atomicInteger = new AtomicInteger(0);
        final var countDownLatch = new CountDownLatch(1);
        long startTime = System.nanoTime();
        underTest.scheduleRunOnce(() -> {
            atomicInteger.incrementAndGet();
            countDownLatch.countDown();
        }, Instant.now().plusMillis(300));

        countDownLatch.await(ASSERTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        assertThat(millisElapsedSince(startTime)).isGreaterThan(300);
        assertThat(atomicInteger.get()).isOne();
    }

    @Test
    void scheduleAtFixedRate() throws InterruptedException {
        final var atomicInteger = new AtomicInteger(0);
        final var countDownLatch = new CountDownLatch(1);
        ScheduledFuture<?> scheduledFuture = underTest.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException success) {
                atomicInteger.incrementAndGet();
                countDownLatch.countDown();
            }

        }, Instant.now(), 300, TimeUnit.MILLISECONDS);

        Thread.sleep(1000);
        scheduledFuture.cancel(true);
        countDownLatch.await(ASSERTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        assertThat(atomicInteger.get()).isEqualTo(1);
    }

    @Test
    void closeExecutor() {
        final var atomicInteger = new AtomicInteger(0);
        underTest.close();
        assertThatExceptionOfType(RejectedExecutionException.class).isThrownBy(
                () -> underTest.scheduleRunOnce(atomicInteger::incrementAndGet, Instant.now()));
    }
}