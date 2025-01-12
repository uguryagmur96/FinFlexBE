package com.finflex.controller;

import com.finflex.entitiy.ExchangeRates;
import com.finflex.service.ExchangeRatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.finflex.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequestMapping(EXCHANGE_RATES)
public class ExchangeRatesController {

    private final ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping(GET_EXCHANGE_RATES)
    public ResponseEntity<ExchangeRates> getExchangeRates(String currencyPair) {
        return ResponseEntity.ok(exchangeRatesService.getExchangeRates(currencyPair));
    }
    @GetMapping(GET_ALL_EXCHANGE_RATES)
    public ResponseEntity<List<ExchangeRates>> getAllExchangeRates(){
        return ResponseEntity.ok(exchangeRatesService.getAllExchangeRates());
    }
}
