package com.jffs.trade.task;

import com.jffs.trade.domain.DataFrame;
import com.jffs.trade.strategy.Strategy;

import java.util.function.Supplier;

public class JobBuilder {
    private static String symbol;
    private Strategy strategy;
    private Supplier<DataFrame> dataFrameSupplier;

    private JobBuilder(String symbol) {
        this.symbol = symbol;
    }

    public static JobBuilder tradeFor(String symbol) {
        return new JobBuilder(symbol);
    }

    public JobBuilder withStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public TradingJob build() {
        return new TradingJob(symbol, dataFrameSupplier, strategy);
    }

    public JobBuilder withInput(Supplier<DataFrame> dataFrameSupplier) {
        this.dataFrameSupplier = dataFrameSupplier;
        return this;
    }
}
