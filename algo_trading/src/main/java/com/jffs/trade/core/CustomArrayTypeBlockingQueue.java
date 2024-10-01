package com.jffs.trade.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/*
 There are 2 conditions where other threads have to be notified.
 a) When queue is full, producers need to wait
 b) When queue is not empty, consumers need to wait
 */

public class CustomArrayTypeBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {

    private int takeIndex;
    private int count;
    private int putIndex;
    private static final int MAX_SIZE = 50;
    private final E[] elements;
    private final ReentrantLock queueLock;
    private final Condition notEmpty;
    private final Condition notFull;

    public CustomArrayTypeBlockingQueue(int size) {
        this.elements = (E[]) new Object[size];
        this.putIndex = 0;
        this.takeIndex = 0;
        this.count = 0;
        queueLock = new ReentrantLock();
        notEmpty = queueLock.newCondition();//For take
        notFull = queueLock.newCondition(); //For put
    }

    public CustomArrayTypeBlockingQueue() {
        this(MAX_SIZE);
    }

    @Override
    public Iterator<E> iterator() {
        queueLock.lock(); //Lock without getting interrupted
        try {
            return new CustomIterator(elements, takeIndex, count);
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public int size() {
        queueLock.lock(); //Lock without getting interrupted
        try {
            return elements == null ? 0 : count;
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public boolean offer(E e) {
        final var lock = this.queueLock;
        lock.lock();
        try {
            if (count == elements.length) {
                return false;
            } else {
                enqueue(e);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    /*
    Should always be called inside a lock
     */
    private void enqueue(E e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }
        elements[putIndex++] = e;
        if (putIndex == elements.length) putIndex = 0;
        notEmpty.signal();
        count++;
    }

    @Override
    public void put(@NotNull E e) throws InterruptedException {
        final ReentrantLock lock = this.queueLock;
        lock.lockInterruptibly();
        queueLock.lockInterruptibly();
        try {
            while (putIndex == elements.length) {
                notFull.await();
            }
            enqueue(e);
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public boolean offer(E e, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        final var lock = this.queueLock;
        lock.lockInterruptibly();
        try {
            while (count == elements.length) {
                if (!notFull.await(timeout, unit)) {
                    return false;
                }
            }
            enqueue(e);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    @Override
    public E take() throws InterruptedException {
        final var lock = queueLock;
        lock.lockInterruptibly();
        try {
            while (count == 0)
                notEmpty.await();

            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    private E dequeue() {
        E e = elements[takeIndex];
        elements[takeIndex] = null;
        if (++takeIndex == elements.length) {
            takeIndex = 0;
        }
        count--;
        notFull.signal();
        return e;
    }

    @Nullable
    @Override
    public E poll(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        final var lock = this.queueLock;
        lock.lockInterruptibly();
        try {
            while (count == 0) {
                if (!notEmpty.await(timeout, unit)) {
                    return null;
                }
            }
            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int remainingCapacity() {
        final var lock = this.queueLock;
        lock.lock();
        try {
            return elements.length - count;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int drainTo(@NotNull Collection<? super E> c) {
        return drainTo(c, c.size());
    }

    @Override
    public int drainTo(@NotNull Collection<? super E> c, int maxElements) {
        if (c == this) {
            throw new IllegalArgumentException();
        }
        return 0;
    }

    @Override
    public E poll() {
        final var lock = this.queueLock;
        lock.lock();
        try {
            if (count == 0) {
                return null;
            } else {
                return dequeue();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E peek() {
        final var lock = this.queueLock;
        lock.lock();
        try {
            return elements[takeIndex];
        } finally {
            lock.unlock();
        }
    }
}
