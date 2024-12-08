package com.jffs.trade.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class Main {
    private static FileAggregator fileAggregator;

    public static void main(String[] args) throws InterruptedException, IOException {
        ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<>();
        final var keepRunning = new AtomicBoolean(true);
        Lock lock = new ReentrantLock();
        File tempFile = File.createTempFile("procons", "test");
        final var fileAggregator = new FileAggregator(tempFile);

        ExecutorService producerExecutors = startProducerThreads(commandQueue, keepRunning, lock, 2, fileAggregator);
        ExecutorService consumerExecutors = startConsumerThreads(commandQueue, keepRunning, lock, 10);

        Thread.sleep(30000);
        keepRunning.compareAndSet(true, false);

        long numberOfRecords = verifyEachRecordIsUniqueIn(tempFile);
        System.out.println("Verification complete for " + tempFile.getAbsolutePath() + " with total records: " + numberOfRecords);

        producerExecutors.shutdown();
        consumerExecutors.shutdown();
    }

    private static long verifyEachRecordIsUniqueIn(File tempFile) throws IOException {
        final var reader = new BufferedReader(new FileReader(tempFile));
        final var uniqueLines = new HashSet<>();

        long numberOfRecords = 0;
        String line = reader.readLine();

        while (line != null) {
            assert !uniqueLines.contains(line);
            uniqueLines.add(line);
            line = reader.readLine();
            numberOfRecords++;
        }
        return numberOfRecords;
    }

    private static ExecutorService startProducerThreads(ConcurrentLinkedQueue<Command> commandQueue,
                                                        AtomicBoolean keepRunning,
                                                        Lock lock,
                                                        int numberOfProducers,
                                                        FileAggregator fileAggregator) {
        List<Runnable> producers = new ArrayList<>();
        for (int i = 0; i < numberOfProducers; i++) {
            producers.add(createProducerThread(keepRunning,
                            ProducerBuilder.aDefaultProducer()
                                    .withQueue(commandQueue)
                                    .withLock(lock)
                                    .withName("producer" + (i + 1))
                                    .build(), fileAggregator));
        }

        final var producerExecutorService = Executors.newFixedThreadPool(numberOfProducers);
        producers.forEach(producerExecutorService::execute);
        return producerExecutorService;
    }

    private static ExecutorService startConsumerThreads(ConcurrentLinkedQueue<Command> commandQueue,
                                                        AtomicBoolean keepRunning,
                                                        Lock queueLock,
                                                        int numberOfConsumers) {
        List<Runnable> consumers = new ArrayList<>();
        for (int i = 0; i < numberOfConsumers; i++) {
            Consumer consumer = ConsumerBuilder.aDefaultConsumer()
                    .withQueue(commandQueue)
                    .withName("consumer" + (i + 1))
                    .build();
            consumer.init(keepRunning);
            consumers.add(consumer);
        }

        final var consumerExecutorService = Executors.newFixedThreadPool(numberOfConsumers);
        consumers.forEach(consumerExecutorService::execute);
        return consumerExecutorService;
    }

    private static Runnable createProducerThread(AtomicBoolean keepRunning,
                                                 Producer producer,
                                                 FileAggregator fileAggregator) {
        Main.fileAggregator = fileAggregator;
        return () -> {
            var counter = 0;
            while (keepRunning.get()) {
                int finalCounter = counter;
                producer.push(new DefaultCommand<>((Function<String, String>) s -> {
                    try {
                        fileAggregator.write(String.format("%s-%d-%s\n", producer.getProducerName(), finalCounter, s));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return "";
                }));

                counter++;
            }
        };
    }
}
