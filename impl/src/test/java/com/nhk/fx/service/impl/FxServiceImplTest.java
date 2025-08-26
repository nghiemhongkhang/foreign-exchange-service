package com.nhk.fx.service.impl;


import com.nhk.fx.client.oanda.OandaClient;
import com.nhk.fx.client.oanda.OandaPoint;
import com.nhk.fx.client.oanda.OandaResponse;
import com.nhk.fx.dto.FxPoint;
import com.nhk.fx.dto.FxRatesResponse;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.mapper.FxMapper;
import com.nhk.fx.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FxServiceImplTest {

    @Mock
    OandaClient oandaClient;
    @Mock
    FxMapper mapper;
    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    FxServiceImpl service;

    @Test
    void getRates_happyPath_withCurrencyNames() {
        String base = "EUR", quote = "USD", start = "2025-03-20", end = "2025-03-27";

        OandaResponse upstream = mock(OandaResponse.class);
        when(oandaClient.fetchRates(base, quote, start, end)).thenReturn(upstream);
        when(upstream.response()).thenReturn(List.of(
                mock(OandaPoint.class)
        ));

        FxPoint fxPoint = mock(FxPoint.class);
        when(mapper.toFxPoints(any())).thenReturn(List.of(fxPoint));

        when(currencyRepository.findById(base)).thenReturn(Optional.of(new Currency(base, "Euro")));
        when(currencyRepository.findById(quote)).thenReturn(Optional.of(new Currency(quote, "US Dollar")));

        when(oandaClient.sourceName()).thenReturn("OANDA FXDS");

        FxRatesResponse res = service.getRates(base, quote, start, end);

        assertThat(res).isNotNull();
        assertThat(res.getBase()).isEqualTo("EUR");
        assertThat(res.getBaseName()).isEqualTo("Euro");
        assertThat(res.getQuote()).isEqualTo("USD");
        assertThat(res.getQuoteName()).isEqualTo("US Dollar");
        assertThat(res.getStartDate()).isEqualTo(start);
        assertThat(res.getEndDate()).isEqualTo(end);
        assertThat(res.getSource()).isEqualTo("OANDA FXDS");
        assertThat(res.getPoints()).hasSize(1);
    }

    @Test
    void getRates_missingCurrencyNames_returnsEmptyStrings() {
        String base = "TWD", quote = "USD", start = "2025-03-26", end = "2025-03-27";

        OandaResponse upstream = mock(OandaResponse.class);
        when(oandaClient.fetchRates(base, quote, start, end)).thenReturn(upstream);
        when(upstream.response()).thenReturn(List.of(mock(OandaPoint.class)));

        when(mapper.toFxPoints(any())).thenReturn(List.of(mock(FxPoint.class)));

        when(currencyRepository.findById(base)).thenReturn(Optional.empty());
        when(currencyRepository.findById(quote)).thenReturn(Optional.empty());

        when(oandaClient.sourceName()).thenReturn("OANDA FXDS");

        FxRatesResponse res = service.getRates(base, quote, start, end);

        assertThat(res).isNotNull();
        assertThat(res.getBase()).isEqualTo("TWD");
        assertThat(res.getBaseName()).isEqualTo("");
        assertThat(res.getQuote()).isEqualTo("USD");
        assertThat(res.getQuoteName()).isEqualTo("");
        assertThat(res.getPoints()).hasSize(1);
    }
}
