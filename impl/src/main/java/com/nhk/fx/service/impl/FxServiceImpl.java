package com.nhk.fx.service.impl;

import com.nhk.fx.client.oanda.OandaClient;
import com.nhk.fx.client.oanda.model.OandaResponse;
import com.nhk.fx.dto.FxRatesResponse;
import com.nhk.fx.mapper.FxMapper;
import com.nhk.fx.service.FxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class FxServiceImpl implements FxService {

    @Autowired
    private OandaClient oandaClient;

    @Autowired
    private FxMapper mapper;

    private static final DateTimeFormatter UPDATE_FMT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneOffset.UTC);

    @Override
    public FxRatesResponse getRates(String base, String quote, String startDate, String endDate) {
        OandaResponse upstream = oandaClient.fetchRates(base, quote, startDate, endDate);

        return new FxRatesResponse(
                UPDATE_FMT.format(Instant.now()),
                oandaClient.sourceName(),
                base,
                quote,
                startDate,
                endDate,
                mapper.toFxPoints(upstream.response())
        );
    }
}
