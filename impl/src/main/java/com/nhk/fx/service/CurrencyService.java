package com.nhk.fx.service;

import com.nhk.fx.dto.CurrencyCreateRequest;
import com.nhk.fx.dto.CurrencyUpdateRequest;
import com.nhk.fx.entity.Currency;
import com.nhk.fx.exception.BusinessException;

import java.util.List;

public interface CurrencyService {

    /**
     * List all currencies sorted by code ascending.
     */
    List<Currency> listAllSortedByCode();

    /**
     * Get a currency by its ISO 4217 code.
     *
     * @throws BusinessException if not found
     */
    Currency getByCode(String code);

    /**
     * Create a new currency.
     *
     * @throws BusinessException if currency already exists
     */
    Currency create(CurrencyCreateRequest currencyCreateRequest);

    /**
     * Update a currency.
     *
     * @param code currency code
     * @param currencyUpdateRequest currencyUpdateRequest
     * @return result object with entity and status (200)
     */
    Currency update(String code, CurrencyUpdateRequest currencyUpdateRequest);

    /**
     * Delete a currency by its code.
     *
     * @throws BusinessException if not found
     */
    void delete(String code);
}
