package com.jffs.trade.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record InstrumentData(String instrument,
                             Granularity granularity,
                             List<CandleData> candles) {
}
