package com.nhk.fx.service.impl;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyResponse;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.exception.model.BusinessException;
import com.nhk.fx.mapper.CurrencyMapper;
import com.nhk.fx.repository.CurrencyRepository;
import com.nhk.fx.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrencyMapper currencyMapper;

    @Override
    public List<CurrencyResponse> listAllSortedByCode() {
        return currencyMapper.toResponseList(currencyRepository.findAll(Sort.by(Sort.Direction.ASC, "code")));
    }

    @Override
    public CurrencyResponse getByCode(String code) {
        return currencyMapper.toResponse(currencyRepository.findById(code)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Currency not found: " + code)));
    }

    @Override
    public CurrencyResponse create(CurrencyCreateRequest currencyCreateRequest) {
        if (currencyRepository.existsById(currencyCreateRequest.getCode())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Currency already exists: " + currencyCreateRequest.getCode());
        }
        var currency = new Currency(currencyCreateRequest.getCode(), currencyCreateRequest.getName());
        return currencyMapper.toResponse(currencyRepository.save(currency));
    }

    @Override
    public CurrencyResponse update(String code, CurrencyUpdateRequest currencyUpdateRequest) {
        var currency = currencyRepository.findById(code)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Currency not found: " + code));
        currency.setName(currencyUpdateRequest.getName());
        return currencyMapper.toResponse(currencyRepository.save(currency));
    }

    @Override
    public void delete(String code) {
        if (!currencyRepository.existsById(code)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Currency not found: " + code);
        }
        currencyRepository.deleteById(code);
    }
}
