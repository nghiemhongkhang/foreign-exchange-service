package com.nhk.fx.service.impl;

import com.nhk.fx.client.oanda.OandaClient;
import com.nhk.fx.client.oanda.OandaResponse;
import com.nhk.fx.dto.FxRatesResponse;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.mapper.FxMapper;
import com.nhk.fx.repository.CurrencyRepository;
import com.nhk.fx.service.FxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class FxServiceImpl implements FxService {

    private final OandaClient oandaClient;
    private final FxMapper mapper;
    private final CurrencyRepository currencyRepository;

    private static final DateTimeFormatter UPDATE_FMT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneOffset.UTC);

    public FxServiceImpl(OandaClient oandaClient,
                         FxMapper mapper,
                         CurrencyRepository currencyRepository) {
        this.oandaClient = oandaClient;
        this.mapper = mapper;
        this.currencyRepository = currencyRepository;
    }


    @Override
    public FxRatesResponse getRates(String base, String quote, String startDate, String endDate) {
        OandaResponse upstream = oandaClient.fetchRates(base, quote, startDate, endDate);

        var baseNameOpt  = currencyRepository.findById(base).map(Currency::getName).orElse(StringUtils.EMPTY);
        var quoteNameOpt = currencyRepository.findById(quote).map(Currency::getName).orElse(StringUtils.EMPTY);

        return new FxRatesResponse(
                UPDATE_FMT.format(Instant.now()),
                oandaClient.sourceName(),
                base,
                baseNameOpt,
                quote,
                quoteNameOpt,
                startDate,
                endDate,
                mapper.toFxPoints(upstream.response())
        );
    }
}
