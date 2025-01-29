package com.jffs.trade.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "oanda")
public record OandaConfig(String host, String apikey) { }
