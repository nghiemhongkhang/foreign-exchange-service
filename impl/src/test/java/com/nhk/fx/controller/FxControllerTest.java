package com.nhk.fx.controller;

import com.nhk.fx.dto.FxRatesResponse;
import com.nhk.fx.service.FxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FxControllerTest {

    @Test
    void getFxRates_returnsOk_andDelegatesToService() {
        FxService fxService = mock(FxService.class);
        FxController controller = new FxController(fxService);

        String base = "EUR";
        String quote = "USD";
        String start = "2025-03-20";
        String end   = "2025-03-27";

        FxRatesResponse expected = mock(FxRatesResponse.class);
        when(fxService.getRates(base, quote, start, end)).thenReturn(expected);

        ResponseEntity<FxRatesResponse> resp = controller.getFxRates(base, start, end, quote);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(expected);
    }
}
