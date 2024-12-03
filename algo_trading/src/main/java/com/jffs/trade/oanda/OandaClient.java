package com.jffs.trade.oanda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jffs.trade.domain.DataFrame;
import com.jffs.trade.oanda.domain.OandaHistoricalData;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OandaClient {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final CloseableHttpClient httpClient;
    private final OandaConfig oandaConfig;
    private final ObjectMapper objectMapper;

    public OandaClient(CloseableHttpClient closeableHttpClient,
                       OandaConfig oandaConfig,
                       ObjectMapper objectMapper) {
        this.httpClient = closeableHttpClient;
        this.oandaConfig = oandaConfig;
        this.objectMapper = objectMapper;
    }

    public DataFrame getHistoricalData(String symbol) {
        LOG.debug("Calling Oanda API");
        final var httpGet = new HttpGet(oandaConfig.host() + "/v3/instruments/" + symbol + "/candles?count=6&price=M&granularity=S5");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + oandaConfig.apikey());

        try {
            String responseString = httpClient.execute(httpGet, new CustomHttpClientResponseHandler());
            LOG.debug("Response: {}", responseString);
            final var oandaHistoricalData = objectMapper.readValue(responseString, OandaHistoricalData.class);
            return oandaHistoricalData.getDataFrame();
        } catch (IOException e) {
            LOG.error("Exception occurred: {}", e.getMessage());
            return null;
        }
    }
}
