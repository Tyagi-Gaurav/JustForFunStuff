package com.jffs.trade.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomIterator<E> implements Iterator<E> {
    private final E[] baseArray;
    private int cursor;

    public CustomIterator(E[] elements) {
        this(elements, 0, elements.length);
    }

    public CustomIterator(E[] elements, int startIndex, int count) {
        this.baseArray = (E[]) new Object[count];
        System.arraycopy(elements, startIndex, baseArray, 0, count);
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor <= baseArray.length - 1;
    }

    @Override
    public E next() {
        if (cursor == baseArray.length) {
            throw new NoSuchElementException();
        }

        return baseArray[cursor++];
    }
}
