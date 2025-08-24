package com.nhk.fx.mapper;


import com.nhk.fx.dto.CurrencyResponse;
import com.nhk.fx.entity.Currency;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyResponse toResponse(Currency entity);

    List<CurrencyResponse> toResponseList(List<Currency> entities);
}
