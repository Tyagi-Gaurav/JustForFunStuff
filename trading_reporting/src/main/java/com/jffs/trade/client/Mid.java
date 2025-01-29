package com.jffs.trade.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record Mid(
        @JsonProperty("h") String high,
        @JsonProperty("l") String low,
        @JsonProperty("o") String open,
        @JsonProperty("c") String close) {
}
