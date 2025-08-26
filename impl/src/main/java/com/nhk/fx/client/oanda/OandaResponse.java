package com.nhk.fx.client.oanda;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OandaResponse(
        @JsonProperty("response") List<OandaPoint> response
) {
}
