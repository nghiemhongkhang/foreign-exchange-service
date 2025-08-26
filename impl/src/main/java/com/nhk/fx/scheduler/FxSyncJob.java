package com.nhk.fx.scheduler;

import com.nhk.fx.client.oanda.OandaClient;
import com.nhk.fx.client.oanda.OandaPoint;
import com.nhk.fx.client.oanda.OandaResponse;
import com.nhk.fx.entity.FxDailyRate;
import com.nhk.fx.repository.CurrencyRepository;
import com.nhk.fx.repository.FxDailyRateRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FxSyncJob {

    private static final Logger log = LoggerFactory.getLogger(FxSyncJob.class);

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);

    private final OandaClient oandaClient;
    private final CurrencyRepository currencyRepository;
    private final FxDailyRateRepository fxDailyRateRepository;

    private final String zone;
    private final int lookbackDays;
    private final String quote;
    private final String excludeBases;
    private final String cronExpr;

    public FxSyncJob(OandaClient oandaClient,
                     CurrencyRepository currencyRepository,
                     FxDailyRateRepository fxDailyRateRepository,
                     @Value("${fxsync.zone:UTC}") String zone,
                     @Value("${fxsync.lookback-days:3}") int lookbackDays,
                     @Value("${fxsync.quote:USD}") String quote,
                     @Value("${fxsync.exclude-bases:USD}") String excludeBases,
                     @Value("${fxsync.cron}") String cronExpr) {
        this.oandaClient = oandaClient;
        this.currencyRepository = currencyRepository;
        this.fxDailyRateRepository = fxDailyRateRepository;
        this.zone = zone;
        this.lookbackDays = lookbackDays;
        this.quote = quote;
        this.excludeBases = excludeBases;
        this.cronExpr = cronExpr;
    }

    @Scheduled(cron = "${fxsync.cron}", zone = "${fxsync.zone:UTC}")
    @Transactional
    public void runDailyBackfill() {
        ZoneId zoneId = ZoneId.of(zone);
        LocalDate end = LocalDate.now(zoneId).minusDays(1);
        LocalDate start = end.minusDays(Math.max(0, lookbackDays - 1));
        log.info("Starting FX sync job. Range={}..{}, quote={}, zone={}, cron={}", start, end, quote, zone, cronExpr);


        Set<String> excluded = Arrays.stream(excludeBases.split(","))
                .map(String::trim).collect(Collectors.toSet());

        List<String> bases = currencyRepository.findAllCodes().stream()
                .filter(Objects::nonNull)
                .filter(c -> !excluded.contains(c))
                .toList();

        for (String base : bases) {
            fetchAndSaveOandaResponse(base, quote, start, end);
        }
    }

    private void fetchAndSaveOandaResponse(String base, String quote,
                                            LocalDate start, LocalDate end) {
        OandaResponse res = oandaClient.fetchRates(base, quote,
                start.toString(), end.toString());
        if (res == null || res.response() == null) {
            return;
        }

        int count = 0;
        for (OandaPoint p : res.response()) {
            LocalDate rateDate =
                    LocalDate.parse(DATE_FMT.format(Instant.parse(p.closeTime())));

            if (fxDailyRateRepository.existsByBaseAndQuoteAndRateDate(base, quote, rateDate)) {
                log.debug("Skip existing {}->{} {}", base, quote, rateDate);
                continue;
            }

            FxDailyRate entity = new FxDailyRate()
                    .setBase(base)
                    .setQuote(quote)
                    .setRateDate(rateDate)
                    .setAverageBid(new BigDecimal(p.averageBid()))
                    .setAverageAsk(new BigDecimal(p.averageAsk()))
                    .setHighBid(new BigDecimal(p.highBid()))
                    .setHighAsk(new BigDecimal(p.highAsk()))
                    .setLowBid(new BigDecimal(p.lowBid()))
                    .setLowAsk(new BigDecimal(p.lowAsk()))
                    .setSource("OANDA FXDS");

            fxDailyRateRepository.save(entity);
            count++;
        }
        if(count > 0) {
            log.info("Synced {} fx points for {}->{} ({}..{}).", count, base, quote, start, end);
        }
    }

}
