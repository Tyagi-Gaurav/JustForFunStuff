package com.jffs.trade.oanda;

import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CustomHttpClientResponseHandler extends BasicHttpClientResponseHandler {
    private static final Logger LOG = LogManager.getLogger("APP");

    @Override
    public String handleResponse(ClassicHttpResponse response) throws IOException {
        if (response.getCode() > 300) {
            LOG.info("Status Code: {}", response.getCode());
            try {
                LOG.info("Response Body: {}", EntityUtils.toString(response.getEntity()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return super.handleResponse(response);
    }
}
