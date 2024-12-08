package com.jffs.trade.monitor;

import java.util.Queue;

public class ConsumerBuilder {

    private Queue<Command> commandQueue;
    private String name;

    public static ConsumerBuilder aDefaultConsumer() {
        return new ConsumerBuilder();
    }

    public ConsumerBuilder withQueue(Queue<Command> commandQueue) {
        this.commandQueue = commandQueue;
        return this;
    }

    public ConsumerBuilder withName(String consumerName) {
        this.name = consumerName;
        return this;
    }

    public Consumer build() {
        return new Consumer(commandQueue, name);
    }
}
