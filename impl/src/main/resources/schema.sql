CREATE TABLE currencies
(
    code       VARCHAR(3) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE fx_daily_rates
(
    id          UUID        NOT NULL PRIMARY KEY,

    base        CHAR(3)     NOT NULL,
    quote       CHAR(3)     NOT NULL,
    rate_date   DATE        NOT NULL,

    average_bid DECIMAL(18, 6),
    average_ask DECIMAL(18, 6),
    high_bid    DECIMAL(18, 6),
    high_ask    DECIMAL(18, 6),
    low_bid     DECIMAL(18, 6),
    low_ask     DECIMAL(18, 6),

    source      VARCHAR(64) NOT NULL,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_fx_daily UNIQUE (base, quote, rate_date)
);