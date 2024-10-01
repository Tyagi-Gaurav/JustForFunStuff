package com.jffs.trade.core;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomArrayTypeBlockingQueueTest {
    private static final long LONG_DELAY = 1000;
    private BlockingQueue<String> underTest = new CustomArrayTypeBlockingQueue<>(1);

    @Test
    void offerNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.offer(null));
    }

    @Test
    void addNullThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.add(null));
    }

    @Test
    void timedOfferNullThrowsException() {
        final var startTime = System.nanoTime();
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.offer(null, LONG_DELAY, MILLISECONDS));
        assertThat(millisElapsedSince(startTime)).isLessThan(LONG_DELAY);
    }

    @Test
    void putNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.put(null));
    }

    @Test
    void drainToNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.drainTo(null));
    }

    @Test
    void drainToSelfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.drainTo(underTest));
    }

    @Test
    void drainToSelfWithCountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.drainTo(underTest, 0));
    }

    @Test
    void drainToNullWithIndexThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> underTest.drainTo(null, 0));
    }

    @Test
    void putBlocks() {
        underTest = emptyCollection();
        final var threadStarted = new CountDownLatch(1);
        final var atomicInteger = new AtomicInteger(1);
        Thread thread = newStartedThread(() -> {
            threadStarted.countDown();
            try {
                underTest.put("test1");
                atomicInteger.incrementAndGet();
            } catch (InterruptedException success) {
            }
            assertFalse(Thread.interrupted());
        });
        await(threadStarted);
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
        awaitTermination(thread);
        assertThat(atomicInteger.get()).describedAs("Expected exception to be thrown").isEqualTo(1);
    }

    @Test
    void takeBlocks() {
        underTest = emptyCollection();
        final var threadStarted = new CountDownLatch(1);
        final var atomicInteger = new AtomicInteger(1);
        Thread thread = newStartedThread(() -> {
            threadStarted.countDown();
            try {
                underTest.take();
                atomicInteger.incrementAndGet();
            } catch (InterruptedException success) {
            }
            assertFalse(Thread.interrupted());
        });
        await(threadStarted);
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
        awaitTermination(thread);
        assertThat(atomicInteger.get()).describedAs("Expected exception to be thrown").isEqualTo(1);
    }

    @Test
    void addThrowsExceptionWhenNoSpaceAvailable() {
        underTest = emptyCollection();
        assertThrows(IllegalStateException.class, () -> underTest.add("testVal"));
    }

    @Test
    void add() {
        underTest = collectionOfSize(1);
        assertThat(underTest).isEmpty();
        underTest.add("testVal");
        assertThat(underTest.size()).isOne();
    }

    @Test
    void pollOnEmptyReturnsNull() {
        underTest = emptyCollection();
        assertThat(underTest.poll()).isNull();
    }

    @Test
    void pollOnNonEmptyReturnsHead() {
        underTest = collectionOfSize(1);
        underTest.add("a");
        assertThat(underTest.poll()).isEqualTo("a");
    }

    @Test
    void offerNoSpace() {
        underTest = emptyCollection();
        assertThat(underTest.offer("testVal")).isFalse();
    }

    /*
      Create a queue.
      Add elements to make it full.
        Separate thread -> Offer with time to wait. 100ms -> Long Delay
      Main thread -> Before long delay, call take()
      Offer should succeed.
      Check the size again.
      Check no exception thrown
      Check offer returns true
      The offer can be interrupted while waiting
     */
    @Test
    void offerWithSpaceToBecomeAvailable() throws InterruptedException {
        underTest = collectionOfSize(1);
        underTest.add("a");
        CountDownLatch offerComplete = new CountDownLatch(1);
        newStartedThread(() -> {
            long startTime = System.nanoTime();
            assertThat(underTest.offer("c", LONG_DELAY, MILLISECONDS)).isTrue(); //Long Delay
            assertThat(millisElapsedSince(startTime)).isLessThan(LONG_DELAY);
            offerComplete.countDown();
        });
        assertThat(underTest.take()).isEqualTo("a");
        offerComplete.await(LONG_DELAY, MILLISECONDS);
        assertThat(underTest.poll()).isEqualTo("c");
    }

    @Test
    void pollWithElementToBeAvailable() throws InterruptedException {
        underTest = collectionOfSize(1);
        CountDownLatch pollComplete = new CountDownLatch(1);
        newStartedThread(() -> {
            long startTime = System.nanoTime();
            assertThat(underTest.poll(LONG_DELAY, MILLISECONDS)).isEqualTo("a"); //Long Delay
            assertThat(millisElapsedSince(startTime)).isLessThan(LONG_DELAY);
            pollComplete.countDown();
        });
        assertThat(underTest.offer("a")).isTrue();
        pollComplete.await(LONG_DELAY, MILLISECONDS);
    }

    @Test
    void pollShouldWaitForSpecifiedTimeBeforeGivingUp() throws InterruptedException {
        underTest = collectionOfSize(1);
        CountDownLatch pollComplete = new CountDownLatch(1);
        newStartedThread(() -> {
            long startTime = System.nanoTime();
            assertThat(underTest.poll(timeoutMillis(), MILLISECONDS)).isNull();
            assertThat(millisElapsedSince(startTime)).isGreaterThanOrEqualTo(timeoutMillis());
            pollComplete.countDown();
        });
        pollComplete.await(LONG_DELAY, MILLISECONDS);
    }

    @Test
    void offerShouldWaitForSpecifiedTimeBeforeGivingUp() throws InterruptedException {
        underTest = collectionOfSize(1);
        assertThat(underTest.offer("b")).isTrue();
        CountDownLatch offerComplete = new CountDownLatch(1);
        newStartedThread(() -> {
            long startTime = System.nanoTime();
            assertThat(underTest.offer("a", timeoutMillis(), MILLISECONDS)).isFalse();
            assertThat(millisElapsedSince(startTime)).isGreaterThanOrEqualTo(timeoutMillis());
            offerComplete.countDown();
        });
        offerComplete.await(timeoutMillis(), MILLISECONDS);
    }

    /**
     * 1. timed poll before a delayed offer times out;
     * 2. Make a delayed offer that succeeds.
     * 3.
     */
    @Test
    void timedPollWithDelayedOffer() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        final var result = new AtomicInteger(1);
        newStartedThread(() -> {
            long startTime = System.nanoTime();
            assertThat(underTest.poll(timeoutMillis(), MILLISECONDS)).isNull(); //Timed pill
            assertThat(millisElapsedSince(startTime)).isGreaterThanOrEqualTo(timeoutMillis());
            cyclicBarrier.await();
            //Timed pull with Long Delay to allow offer made outside the test to succeed.
            assertThat(underTest.poll(LONG_DELAY, MILLISECONDS)).isEqualTo("a");
            result.incrementAndGet();
            cyclicBarrier.await();
        });
        cyclicBarrier.await();
        assertThat(underTest.offer("a", LONG_DELAY, MILLISECONDS)).isTrue();
        cyclicBarrier.await();
        assertThat(result.get()).isEqualTo(2);
    }

    @Test
    void peek() {
        underTest = collectionOfSize(1);
        assertThat(underTest.offer("b")).isTrue();
        assertThat(underTest.peek()).isEqualTo("b");
    }

    @Test
    void remainingCapacity() {
        underTest = collectionOfSize(3);
        assertThat(underTest.offer("b")).isTrue();
        assertThat(underTest.remainingCapacity()).isEqualTo(2);
    }

    @Test
    void iterator() {
        underTest = collectionOfSize(4);
        assertThat(underTest.addAll(List.of("a", "b", "c", "d"))).isTrue();
        assertThat(underTest.poll()).isEqualTo("a");

        Iterator<String> iterator = underTest.iterator();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("b");

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("c");

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo("d");

        assertThat(iterator.hasNext()).isFalse();
    }

    private BlockingQueue<String> collectionOfSize(int size) {
        return new CustomArrayTypeBlockingQueue<>(size);
    }

    private void awaitTermination(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void await(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private BlockingQueue<String> emptyCollection() {
        return new CustomArrayTypeBlockingQueue<>(0);
    }

    private long timeoutMillis() {
        return 100;
    }

    private Thread newStartedThread(CheckedRunnable runnable) {
        Thread thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        return thread;
    }

    private long millisElapsedSince(long startTime) {
        return (System.nanoTime() - startTime) / (1000 * 1000);
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }
}