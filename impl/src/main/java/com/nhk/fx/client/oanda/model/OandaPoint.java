package com.nhk.fx.client.oanda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OandaPoint(
        @JsonProperty("base_currency") String baseCurrency,
        @JsonProperty("quote_currency") String quoteCurrency,
        @JsonProperty("close_time") String closeTime,
        @JsonProperty("average_bid") String averageBid,
        @JsonProperty("average_ask") String averageAsk,
        @JsonProperty("high_bid") String highBid,
        @JsonProperty("high_ask") String highAsk,
        @JsonProperty("low_bid") String lowBid,
        @JsonProperty("low_ask") String lowAsk
) {
}

