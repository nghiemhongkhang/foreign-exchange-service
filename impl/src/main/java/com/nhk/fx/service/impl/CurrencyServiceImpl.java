package com.nhk.fx.service.impl;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.exception.BusinessException;
import com.nhk.fx.repository.CurrencyRepository;
import com.nhk.fx.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    @Override
    public List<Currency> listAllSortedByCode() {
        return currencyRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
    }

    @Override
    public Currency getByCode(String code) {
        return currencyRepository.findById(code)
                .orElseThrow(() -> new BusinessException(404, "Not Found", "Currency not found: " + code));
    }

    @Override
    public Currency create(CurrencyCreateRequest currencyCreateRequest) {
        if (currencyRepository.existsById(currencyCreateRequest.getCode())) {
            throw new BusinessException(409, "Conflict", "Currency already exists: " + currencyCreateRequest.getCode());
        }
        Currency currency = new Currency(currencyCreateRequest.getCode(), currencyCreateRequest.getName());
        return currencyRepository.save(currency);
    }

    @Override
    public Currency update(String code, CurrencyUpdateRequest currencyUpdateRequest) {
        Currency currency = currencyRepository.findById(code)
                .orElseThrow(() -> new BusinessException(404, "Not Found", "Currency not found: " + code));
        currency.setName(currencyUpdateRequest.getName());
        return currencyRepository.save(currency);
    }

    @Override
    public void delete(String code) {
        if (!currencyRepository.existsById(code)) {
            throw new BusinessException(404, "Not Found", "Currency not found: " + code);
        }
        currencyRepository.deleteById(code);
    }
}
