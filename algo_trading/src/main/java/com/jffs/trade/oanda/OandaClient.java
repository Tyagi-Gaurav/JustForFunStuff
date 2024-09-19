package com.jffs.trade.oanda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.trade.oanda.domain.OandaHistoricalData;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OandaClient {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final CloseableHttpClient httpClient;
    private final OandaConfig oandaConfig;
    private final ObjectMapper objectMapper;

    @Autowired
    public OandaClient(CloseableHttpClient closeableHttpClient,
                       OandaConfig oandaConfig,
                       ObjectMapper objectMapper) {
        this.httpClient = closeableHttpClient;
        this.oandaConfig = oandaConfig;
        this.objectMapper = objectMapper;
    }

    public OandaHistoricalData getHistoricalData(String symbol) {
        LOG.debug("Calling Oanda API");
        final var httpGet = new HttpGet(oandaConfig.host() + "/v3/instruments/" + symbol + "/candles?count=6&price=M&granularity=S5");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + oandaConfig.apikey());

        try {
            String responseString = httpClient.execute(httpGet, new CustomHttpClientResponseHandler());
            LOG.debug("Response: {}", responseString);
            return objectMapper.readValue(responseString, OandaHistoricalData.class);
        } catch (IOException e) {
            LOG.error("Exception occurred: {}", e.getMessage());
            return null;
        }
    }
}
