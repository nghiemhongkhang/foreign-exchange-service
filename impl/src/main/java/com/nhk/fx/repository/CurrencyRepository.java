package com.nhk.fx.repository;

import com.nhk.fx.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    @Query("select c.code from Currency c")
    List<String> findAllCodes();
}
