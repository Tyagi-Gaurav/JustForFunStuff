package com.jffs.trade;

import com.jffs.trade.client.CandleData;
import com.jffs.trade.client.InstrumentData;
import com.jffs.trade.client.InstrumentDataRetriever;
import com.jffs.trade.client.OandaClient;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.jooq.generated.tables.CandleS5.CANDLE_S5;

@Component
public class ScheduledDataRetriever implements InstrumentDataRetriever {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private OandaClient oandaClient;

    @Scheduled(cron = "${scheduled.data.retriever.cron.expression}")
    public void schedule() {
        saveToDB(this.retrieveM1Data());
    }

    private void saveToDB(InstrumentData instrumentData) {
        CandleData candleData = instrumentData.candles()
                .get(0);
        dslContext.insertInto(CANDLE_S5,
                        CANDLE_S5.CANDLE_S5_ID,
                        CANDLE_S5.INSTRUMENT,
                        CANDLE_S5.RECORDED_TIMESTAMP,
                        CANDLE_S5.PRICE_HIGH,
                        CANDLE_S5.PRICE_LOW,
                        CANDLE_S5.PRICE_CLOSE,
                        CANDLE_S5.PRICE_OPEN)
                .values(UUID.randomUUID().toString(),
                        instrumentData.instrument(),
                        candleData.time(),
                        candleData.mid().high(),
                        candleData.mid().low(),
                        candleData.mid().close(),
                        candleData.mid().open())
                .execute();
    }

    @Override
    public InstrumentData retrieveM1Data() {
        InstrumentData instrumentData = oandaClient.retrieveRateData("M1");
        System.out.println(instrumentData);
        return instrumentData;
    }
}
