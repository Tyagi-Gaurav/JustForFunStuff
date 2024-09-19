package com.jffs.trade.oanda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OandaHistoricalData(String instrument,
                                  String granularity,
                                  List<Candle> candles) {
}
