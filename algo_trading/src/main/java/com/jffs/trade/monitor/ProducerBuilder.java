package com.jffs.trade.monitor;

import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class ProducerBuilder {
    private Queue<Command> commandQueue;
    private String producerName;
    private Lock lock;

    public static ProducerBuilder aDefaultProducer() {
        return new ProducerBuilder();
    }

    public ProducerBuilder withQueue(Queue<Command> commandQueue) {
        this.commandQueue = commandQueue;
        return this;
    }

    public ProducerBuilder withName(String producerName) {
        this.producerName = producerName;
        return this;
    }

    public ProducerBuilder withLock(Lock lock) {
        this.lock = lock;
        return this;
    }

    public Producer build() {
        return new Producer(
                commandQueue,
                lock,
                producerName);
    }
}
