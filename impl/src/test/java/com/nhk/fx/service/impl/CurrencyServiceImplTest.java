package com.nhk.fx.service.impl;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyResponse;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.exception.model.BusinessException;
import com.nhk.fx.mapper.CurrencyMapper;
import com.nhk.fx.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    CurrencyRepository repo;

    @Mock
    CurrencyMapper mapper;

    @InjectMocks
    CurrencyServiceImpl service;

    @Test
    void listAllSortedByCode_ok() {
        CurrencyResponse resp = mock(CurrencyResponse.class);

        when(repo.findAll(any(Sort.class))).thenReturn(List.of(new Currency("EUR", "Euro")));
        when(mapper.toResponseList(any())).thenReturn(List.of(resp));

        List<CurrencyResponse> result = service.listAllSortedByCode();

        assertThat(result).hasSize(1);
    }

    @Test
    void getByCode_found() {
        Currency eur = new Currency("EUR", "Euro");
        CurrencyResponse resp = mock(CurrencyResponse.class);
        when(repo.findById("EUR")).thenReturn(Optional.of(eur));
        when(mapper.toResponse(eur)).thenReturn(resp);

        var result = service.getByCode("EUR");

        assertThat(result).isSameAs(resp);
    }

    @Test
    void getByCode_notFound() {
        when(repo.findById("X")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByCode("X"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Currency not found")
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void create_ok() {
        CurrencyCreateRequest req = mock(CurrencyCreateRequest.class);
        when(req.getCode()).thenReturn("EUR");
        when(req.getName()).thenReturn("Euro");
        when(repo.existsById("EUR")).thenReturn(false);
        when(repo.save(any())).thenReturn(new Currency("EUR", "Euro"));
        when(mapper.toResponse(any())).thenReturn(mock(CurrencyResponse.class));

        var result = service.create(req);

        assertThat(result).isNotNull();
    }

    @Test
    void create_conflict() {
        CurrencyCreateRequest req = mock(CurrencyCreateRequest.class);
        when(req.getCode()).thenReturn("EUR");
        when(repo.existsById("EUR")).thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(BusinessException.class)
                .extracting("status").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void update_ok() {
        CurrencyUpdateRequest req = mock(CurrencyUpdateRequest.class);
        when(req.getName()).thenReturn("Eurozone Euro");
        Currency eur = new Currency("EUR", "Euro");
        when(repo.findById("EUR")).thenReturn(Optional.of(eur));
        when(repo.save(any())).thenReturn(eur);
        when(mapper.toResponse(eur)).thenReturn(mock(CurrencyResponse.class));

        var result = service.update("EUR", req);

        assertThat(result).isNotNull();
    }

    @Test
    void update_notFound() {
        when(repo.findById("X")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("X", mock(CurrencyUpdateRequest.class)))
                .isInstanceOf(BusinessException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_ok() {
        when(repo.existsById("EUR")).thenReturn(true);

        service.delete("EUR");

        verify(repo).deleteById("EUR");
    }

    @Test
    void delete_notFound() {
        when(repo.existsById("X")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("X"))
                .isInstanceOf(BusinessException.class)
                .extracting("status").isEqualTo(HttpStatus.NOT_FOUND);
    }
}
