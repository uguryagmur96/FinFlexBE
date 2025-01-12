package com.finflex.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.finflex.dto.request.GetExchangeRatesRequest;
import com.finflex.entitiy.ExchangeRates;
import com.finflex.repository.IExchangeRatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeRatesServiceTest {
    @Mock
    private IExchangeRatesRepository exchangeRatesRepository;

    @InjectMocks
    private ExchangeRatesService exchangeRatesService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPairExchangeRates_existingRateUpdate() {

        String url = "someApiUrl";
        GetExchangeRatesRequest response = new GetExchangeRatesRequest();
        response.setResult("success");
        response.setBase_code("USD");
        response.setTarget_code("EUR");
        response.setConversion_rate(BigDecimal.valueOf(0.85));

        ExchangeRates existingRate = new ExchangeRates();
        existingRate.setId(1L);
        existingRate.setCurrencyPair("USDEUR");
        existingRate.setRate(BigDecimal.valueOf(0.80));

        when(restTemplate.getForObject(anyString(), eq(GetExchangeRatesRequest.class)))
                .thenReturn(response);
        when(exchangeRatesRepository.findOptionalByCurrencyPair("USDEUR"))
                .thenReturn(Optional.of(existingRate));

    }

    @Test
    void testGetPairExchangeRates_apiException() {
        when(restTemplate.getForObject(anyString(), eq(GetExchangeRatesRequest.class)))
                .thenThrow(new RestClientException("API error"));

        verify(exchangeRatesRepository, never()).save(any(ExchangeRates.class));
    }
}
