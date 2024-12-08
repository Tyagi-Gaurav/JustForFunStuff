package com.jffs.trade.strategy;

import com.jffs.trade.domain.DataFrame;

import java.util.List;

public record Strategy<T extends DataFrame>(String symbol, List<Task<T>> tasks, Condition condition) {
    public void apply(ExecutionContext<T> executionContext) {
        var candidateDataFrame = executionContext;
        for (Task<T> task : tasks) {
            candidateDataFrame = task.execute(candidateDataFrame);
        }
    }
}
