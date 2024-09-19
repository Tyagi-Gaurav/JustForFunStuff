package com.jffs.trade.oanda;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oanda")
public record OandaConfig(String host,
                          String apikey) {
}
