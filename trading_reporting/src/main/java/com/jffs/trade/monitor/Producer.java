package com.jffs.trade.monitor;

import java.util.Queue;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Producer<T> {
    private final Queue<T> targetQueue;
    private final Lock lock;
    private final String producerName;

    public Producer(Queue<T> commandQueue,
                    Lock lock,
                    String producerName) {
        this.targetQueue = commandQueue;
        this.lock = lock;
        this.producerName = producerName;
    }

    public void push(T defaultCommand) {
        try {
            if (lock.tryLock(100, MILLISECONDS)) {
                try {
                    if (!targetQueue.offer(defaultCommand)) {
                        throw new QueueFullException(producerName + " unable to insert element in the queue. Current Size: " + targetQueue.size());
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public String getProducerName() {
        return producerName;
    }
}
