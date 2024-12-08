package com.jffs.trade.monitor;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
    private final Queue<Command> commandQueue;
    private final String name;
    private AtomicBoolean keepRunning;

    public <T> Consumer(Queue<Command> commandQueue,
                        String name) {
        this.commandQueue = commandQueue;
        this.name = name;
    }

    @Override
    public void run() {
        while (keepRunning.get()) {
            var command = commandQueue.poll();
            if (command != null) {
                command.execute(name);
            }
        }
    }

    public void init(AtomicBoolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}
