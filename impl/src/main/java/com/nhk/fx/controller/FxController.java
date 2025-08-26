package com.nhk.fx.controller;

import com.nhk.fx.dto.FxRatesResponse;
import com.nhk.fx.service.FxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FxController implements FxApi{

    private final FxService fxService;

    public FxController(FxService fxService) {
        this.fxService = fxService;
    }

    @Override
    public ResponseEntity<FxRatesResponse> getFxRates(String base, String startDate, String endDate, String quote) {
        return ResponseEntity.status(HttpStatus.OK).body(fxService.getRates(base, quote, startDate, endDate));
    }
}
