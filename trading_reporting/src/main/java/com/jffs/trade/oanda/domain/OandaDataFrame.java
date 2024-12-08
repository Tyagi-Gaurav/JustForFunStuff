package com.jffs.trade.oanda.domain;

import com.jffs.trade.domain.DataFrame;

import java.util.List;

public record OandaDataFrame(String instrument, List<Candle> candles) implements DataFrame {
}
