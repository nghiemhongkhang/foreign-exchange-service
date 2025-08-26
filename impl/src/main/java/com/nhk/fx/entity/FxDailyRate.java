package com.nhk.fx.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fx_daily_rates")
public class FxDailyRate {

    @Id
    @GeneratedValue
    private UUID id;

    private String base;
    private String quote;
    private LocalDate rateDate;

    @Column(precision = 18, scale = 6)
    private BigDecimal averageBid;
    @Column(precision = 18, scale = 6)
    private BigDecimal averageAsk;
    @Column(precision = 18, scale = 6)
    private BigDecimal highBid;
    @Column(precision = 18, scale = 6)
    private BigDecimal highAsk;
    @Column(precision = 18, scale = 6)
    private BigDecimal lowBid;
    @Column(precision = 18, scale = 6)
    private BigDecimal lowAsk;

    @Column(nullable = false)
    private String source;
    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public FxDailyRate setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getBase() {
        return base;
    }

    public FxDailyRate setBase(String base) {
        this.base = base;
        return this;
    }

    public String getQuote() {
        return quote;
    }

    public FxDailyRate setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    public FxDailyRate setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
        return this;
    }

    public BigDecimal getAverageBid() {
        return averageBid;
    }

    public FxDailyRate setAverageBid(BigDecimal averageBid) {
        this.averageBid = averageBid;
        return this;
    }

    public BigDecimal getAverageAsk() {
        return averageAsk;
    }

    public FxDailyRate setAverageAsk(BigDecimal averageAsk) {
        this.averageAsk = averageAsk;
        return this;
    }

    public BigDecimal getHighBid() {
        return highBid;
    }

    public FxDailyRate setHighBid(BigDecimal highBid) {
        this.highBid = highBid;
        return this;
    }

    public BigDecimal getHighAsk() {
        return highAsk;
    }

    public FxDailyRate setHighAsk(BigDecimal highAsk) {
        this.highAsk = highAsk;
        return this;
    }

    public BigDecimal getLowBid() {
        return lowBid;
    }

    public FxDailyRate setLowBid(BigDecimal lowBid) {
        this.lowBid = lowBid;
        return this;
    }

    public BigDecimal getLowAsk() {
        return lowAsk;
    }

    public FxDailyRate setLowAsk(BigDecimal lowAsk) {
        this.lowAsk = lowAsk;
        return this;
    }

    public String getSource() {
        return source;
    }

    public FxDailyRate setSource(String source) {
        this.source = source;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public FxDailyRate setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
