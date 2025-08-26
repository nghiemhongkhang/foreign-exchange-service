package com.nhk.fx.scheduler;

import com.nhk.fx.client.oanda.OandaClient;
import com.nhk.fx.client.oanda.OandaPoint;
import com.nhk.fx.client.oanda.OandaResponse;
import com.nhk.fx.entity.FxDailyRate;
import com.nhk.fx.repository.CurrencyRepository;
import com.nhk.fx.repository.FxDailyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FxSyncJobTest {

    @Mock
    OandaClient oandaClient;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    FxDailyRateRepository fxDailyRateRepository;

    private FxSyncJob job;

    @BeforeEach
    void setUp() {
        job = new FxSyncJob(
                oandaClient,
                currencyRepository,
                fxDailyRateRepository,
                "UTC",          // zone
                1,              // lookbackDays
                "USD",          // quote
                "USD",          // excludeBases
                "* * * * * *"   // cronExpr
        );
    }

    @Test
    void runDailyBackfill_insertsNew() {
        when(currencyRepository.findAllCodes()).thenReturn(List.of("EUR"));
        OandaPoint point = mock(OandaPoint.class);
        when(point.closeTime()).thenReturn("2025-03-26T23:59:59Z");
        when(point.averageBid()).thenReturn("1.10");
        when(point.averageAsk()).thenReturn("1.11");
        when(point.highBid()).thenReturn("1.12");
        when(point.highAsk()).thenReturn("1.13");
        when(point.lowBid()).thenReturn("1.09");
        when(point.lowAsk()).thenReturn("1.08");
        OandaResponse resp = mock(OandaResponse.class);
        when(resp.response()).thenReturn(List.of(point));
        when(oandaClient.fetchRates(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resp);
        when(fxDailyRateRepository.existsByBaseAndQuoteAndRateDate(any(), any(), any()))
                .thenReturn(false);

        job.runDailyBackfill();

        verify(oandaClient).fetchRates(eq("EUR"), eq("USD"), anyString(), anyString());
        verify(fxDailyRateRepository).save(any(FxDailyRate.class));
    }

    @Test
    void runDailyBackfill_skipsExisting() {
        when(currencyRepository.findAllCodes()).thenReturn(List.of("EUR"));
        OandaPoint point = mock(OandaPoint.class);
        when(point.closeTime()).thenReturn("2025-03-26T23:59:59Z");
        OandaResponse resp = mock(OandaResponse.class);
        when(resp.response()).thenReturn(List.of(point));
        when(oandaClient.fetchRates(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(resp);
        when(fxDailyRateRepository.existsByBaseAndQuoteAndRateDate(any(), any(), any()))
                .thenReturn(true);

        job.runDailyBackfill();

        verify(fxDailyRateRepository, never()).save(any());
    }
}
