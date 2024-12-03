package com.jffs.trade.strategy;

@FunctionalInterface
public interface Task<T> {
    ExecutionContext<T> execute(ExecutionContext<T> dataFrame);
}
