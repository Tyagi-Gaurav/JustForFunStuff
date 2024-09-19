package com.jffs.trade.monitor;

public interface Command<T, R> {
    R execute(T t);
}
