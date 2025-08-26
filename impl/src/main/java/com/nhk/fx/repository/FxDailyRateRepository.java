package com.nhk.fx.repository;

import com.nhk.fx.entity.FxDailyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface FxDailyRateRepository extends JpaRepository<FxDailyRate, UUID> {
    boolean existsByBaseAndQuoteAndRateDate(String base, String quote, LocalDate rateDate);
}
