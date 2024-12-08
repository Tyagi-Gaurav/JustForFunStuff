package com.jffs.trade.task;

import com.jffs.trade.domain.DataFrame;
import com.jffs.trade.strategy.ExecutionContext;
import com.jffs.trade.strategy.Strategy;

import java.util.function.Supplier;

public class TradingRunner {
    private final String symbol;
    private final Supplier<DataFrame> dataFrameSupplier;
    private final Strategy<DataFrame> strategy;

    public TradingRunner(String symbol, Supplier<DataFrame> dataFrameSupplier, Strategy<DataFrame> strategy) {
        this.symbol = symbol;
        this.dataFrameSupplier = dataFrameSupplier;
        this.strategy = strategy;
    }

    public void start() {
        DataFrame dataFrame = dataFrameSupplier.get();
        strategy.apply(new ExecutionContext<>(dataFrame));
    }
}
