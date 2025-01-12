package com.finflex.repository;

import com.finflex.entitiy.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IExchangeRatesRepository extends JpaRepository<ExchangeRates,Long> {
    ExchangeRates findByCurrencyPair(String currencyPair);
    Optional<ExchangeRates> findOptionalByCurrencyPair(String currencyPair);
}
