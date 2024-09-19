package com.jffs.trade.oanda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Candle(int volume,
                     String time,
                     Mid mid) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Mid(
            @JsonProperty("o") double open,
            @JsonProperty("h") double high,
            @JsonProperty("l") double low,
            @JsonProperty("c") double close) {
    }
}
