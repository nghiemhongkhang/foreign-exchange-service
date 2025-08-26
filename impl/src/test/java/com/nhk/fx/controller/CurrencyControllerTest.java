package com.nhk.fx.controller;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyResponse;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController controller;

    private CurrencyCreateRequest createReq;
    private CurrencyUpdateRequest updateReq;
    private CurrencyResponse eurResp;
    private CurrencyResponse usdResp;

    @BeforeEach
    void setUp() {
        createReq = mock(CurrencyCreateRequest.class);
        updateReq = mock(CurrencyUpdateRequest.class);
        eurResp = mock(CurrencyResponse.class);
        usdResp = mock(CurrencyResponse.class);
    }

    @Test
    void createCurrency_returns201_andBody() {
        when(currencyService.create(createReq)).thenReturn(eurResp);

        ResponseEntity<CurrencyResponse> resp = controller.createCurrency(createReq);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        assertThat(resp.getBody()).isSameAs(eurResp);
        verify(currencyService).create(createReq);
    }

    @Test
    void deleteCurrency_returns204_andInvokesService() {
        String code = "EUR";

        ResponseEntity<Void> resp = controller.deleteCurrency(code);

        assertThat(resp.getStatusCode().value()).isEqualTo(204);
        assertThat(resp.getBody()).isNull();
        verify(currencyService).delete(code);
    }

    @Test
    void getCurrencyByCode_returns200_andBody() {
        String code = "EUR";
        when(currencyService.getByCode(code)).thenReturn(eurResp);

        ResponseEntity<CurrencyResponse> resp = controller.getCurrencyByCode(code);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(eurResp);
        verify(currencyService).getByCode(code);
    }

    @Test
    void listCurrencies_returns200_andList() {
        List<CurrencyResponse> list = List.of(eurResp, usdResp);
        when(currencyService.listAllSortedByCode()).thenReturn(list);

        ResponseEntity<List<CurrencyResponse>> resp = controller.listCurrencies();

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        verify(currencyService).listAllSortedByCode();
    }

    @Test
    void updateCurrency_returns200_andBody() {
        String code = "EUR";
        when(currencyService.update(code, updateReq)).thenReturn(eurResp);

        ResponseEntity<CurrencyResponse> resp = controller.updateCurrency(code, updateReq);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(eurResp);
        verify(currencyService).update(code, updateReq);
    }
}
