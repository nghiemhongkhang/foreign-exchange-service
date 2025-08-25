package com.nhk.fx.service;

import com.nhk.fx.dto.FxRatesResponse;

public interface FxService {
    FxRatesResponse getRates(String base, String quote, String startDate, String endDate);
}
