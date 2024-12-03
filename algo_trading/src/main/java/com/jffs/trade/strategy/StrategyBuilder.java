package com.jffs.trade.strategy;

import java.util.List;

public class StrategyBuilder {
    private List<Task> tasks;
    private String symbol;

    private StrategyBuilder() {}

    public static StrategyBuilder aStrategy() {
        return new StrategyBuilder();
    }

    public StrategyBuilder withTask(Task task) {
        this.tasks.add(task);
        return this;
    }

    public StrategyBuilder with(Condition condition) {
        return this;
    }

    public Strategy build() {
        return new Strategy(symbol, tasks, null);
    }

    public StrategyBuilder usingSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }
}
