package com.jffs.trade.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//TODO Test
@Component
public class OandaClient {
    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Autowired
    private OandaConfig oandaConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InstrumentData retrieveRateData(String rate) {
        return switch (rate) {
            case "M1" -> retrieveM1Rate("S5", "EUR_USD");
            default -> throw new RuntimeException();
        };
    }

    private InstrumentData retrieveM1Rate(String granularity, String instrument) {
        HttpClientResponseHandler<InstrumentData> responseHandler = classicHttpResponse -> {
            final var responseAsString = EntityUtils.toString(classicHttpResponse.getEntity(), StandardCharsets.UTF_8);
            System.out.println(responseAsString);
            return objectMapper.readValue(responseAsString, InstrumentData.class);
        };
        try {
            String uri = "/v3/instruments/%s/candles?granularity=%s&count=100"
                    .formatted(instrument,
                            granularity);
            final var httpGet = new HttpGet(uri);
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oandaConfig.apikey());
            return closeableHttpClient.execute(
                    HttpHost.create(oandaConfig.host()),
                    httpGet,
                    responseHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
