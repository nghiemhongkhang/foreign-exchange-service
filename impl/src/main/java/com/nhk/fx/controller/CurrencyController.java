package com.nhk.fx.controller;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyResponse;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.mapper.CurrencyMapper;
import com.nhk.fx.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController implements CurrenciesApi {

    @Autowired
    CurrencyService currencyService;

    @Autowired
    CurrencyMapper currencyMapper;

    @Override
    public ResponseEntity<CurrencyResponse> createCurrency(CurrencyCreateRequest currencyCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyMapper.toResponse(currencyService.create(currencyCreateRequest)));
    }

    @Override
    public ResponseEntity<Void> deleteCurrency(String code) {
        currencyService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CurrencyResponse> getCurrencyByCode(String code) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyMapper.toResponse(currencyService.getByCode(code)));
    }

    @Override
    public ResponseEntity<List<CurrencyResponse>> listCurrencies() {
        return ResponseEntity.status(HttpStatus.OK).body(currencyMapper.toResponseList(currencyService.listAllSortedByCode()));
    }

    @Override
    public ResponseEntity<CurrencyResponse> updateCurrency(String code, CurrencyUpdateRequest currencyUpdateRequest) {
        return ResponseEntity.ok(currencyMapper.toResponse(currencyService.update(code, currencyUpdateRequest)));
    }
}
