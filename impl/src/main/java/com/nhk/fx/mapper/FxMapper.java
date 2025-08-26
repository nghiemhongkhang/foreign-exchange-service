package com.nhk.fx.mapper;

import com.nhk.fx.client.oanda.OandaPoint;
import com.nhk.fx.dto.FxPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FxMapper {

    DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);

    @Mappings({
            @Mapping(target = "date", expression = "java(toDate(p.closeTime()))"),
            @Mapping(target = "averageBid", expression = "java(toBigDecimal(p.averageBid()))"),
            @Mapping(target = "averageAsk", expression = "java(toBigDecimal(p.averageAsk()))"),
            @Mapping(target = "highBid", expression = "java(toBigDecimal(p.highBid()))"),
            @Mapping(target = "highAsk", expression = "java(toBigDecimal(p.highAsk()))"),
            @Mapping(target = "lowBid", expression = "java(toBigDecimal(p.lowBid()))"),
            @Mapping(target = "lowAsk", expression = "java(toBigDecimal(p.lowAsk()))")
    })
    FxPoint toFxPoint(OandaPoint p);

    List<FxPoint> toFxPoints(List<OandaPoint> points);

    default String toDate(String closeTime) {
        return DATE_FMT.format(Instant.parse(closeTime));
    }

    default BigDecimal toBigDecimal(String s) {
        return (s == null) ? null : new BigDecimal(s);
    }
}
