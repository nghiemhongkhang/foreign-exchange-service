package com.nhk.fx.client.oanda;

import com.nhk.fx.client.oanda.model.OandaResponse;
import com.nhk.fx.exception.model.UpstreamException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class OandaClient {
    private final WebClient client;

    public OandaClient(WebClient oandaWebClient) {
        this.client = oandaWebClient;
    }

    public OandaResponse fetchRates(String base, String quote, String startDate, String endDate) {
        try {
            return client.get()
                    .uri(uri -> uri.path("/cc-api/currencies")
                            .queryParam("base", base)
                            .queryParam("quote", quote)
                            .queryParam("data_type", "chart")
                            .queryParam("start_date", startDate)
                            .queryParam("end_date", endDate)
                            .build())
                    .retrieve()
                    .bodyToMono(OandaResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new UpstreamException("OANDA error: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new UpstreamException("OANDA call failed", ex);
        }
    }

    public String sourceName() {
        return "OANDA FXDS";
    }

}
