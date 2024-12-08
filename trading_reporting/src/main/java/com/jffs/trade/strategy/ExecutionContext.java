package com.jffs.trade.strategy;

public class ExecutionContext<T> {
    private T t;

    public ExecutionContext(T t) {
        this.t = t;
    }

    public T getExecutionData() {
        return t;
    }

    public void updateExecutionData(T newT) {
        this.t = newT;
    }

}
