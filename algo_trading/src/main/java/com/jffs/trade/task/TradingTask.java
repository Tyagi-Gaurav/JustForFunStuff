package com.jffs.trade.task;

import com.jffs.trade.core.CustomThreadPoolScheduler;
import com.jffs.trade.domain.DataFrame;
import com.jffs.trade.oanda.OandaClient;
import com.jffs.trade.strategy.ExecutionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.jffs.trade.strategy.StrategyBuilder.aStrategy;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class TradingTask implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOG = LogManager.getLogger("APP");
    private final OandaClient oandaClient;

    public TradingTask(OandaClient oandaClient,
                       CustomThreadPoolScheduler customThreadPoolScheduler) {
        this.oandaClient = oandaClient;
        LOG.info("Scheduling task");
        customThreadPoolScheduler
                .scheduleAtFixedRate(() -> createTask("EUR_USD"), now().plusSeconds(1), 5 * 1000, MILLISECONDS);
    }

    /*
    Should be reading tasks from a factory/config
     */
    public void createTask(String symbol) {
        final var strategy = aStrategy()
                .withTask(this::calculateSma20)
                .withTask(this::calculateSma50)
                .withTask(this::determineCrossover)
                .withTask(this::calculateATR)
                .with(Condition.allOf(allowedBuyTrading, ))
//                .when(allOf(allowedBuyTrading(), ), then(performBuyTrade()))listOf(df -> smaCrossover(20, 50), () -> allowedBuyTrading()),
//                        then(df -> performBuyTrade()))
                .build();

        TradingJob job = JobBuilder.tradeFor(symbol)
                .withInput(() -> oandaClient.getHistoricalData(symbol))
                .withStrategy(strategy)
                .build();

        job.start();
    }

    private ExecutionContext<DataFrame> calculateATR(ExecutionContext<DataFrame> dataFrame) {
        System.out.println("Apply 20");
        return dataFrame;
    }

    private ExecutionContext<DataFrame> determineCrossover(ExecutionContext<DataFrame> dataFrame) {
        System.out.println("Apply 20");
        return dataFrame;
    }

    private ExecutionContext<DataFrame> calculateSma20(ExecutionContext<DataFrame> dataFrame) {
        System.out.println("Apply 20");
        return dataFrame;
    }

    private ExecutionContext<DataFrame> calculateSma50(ExecutionContext<DataFrame> dataFrame) {
        System.out.println("Apply 50");
        return dataFrame;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOG.info("Starting Jobs..");
    }
}
