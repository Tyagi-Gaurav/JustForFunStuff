package com.jffs.trade.oanda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jffs.trade.domain.DataFrame;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OandaHistoricalData(String instrument,
                                  String granularity,
                                  List<Candle> candles) {
    public DataFrame getDataFrame() {
        return new OandaDataFrame(instrument, candles);
    }
}
