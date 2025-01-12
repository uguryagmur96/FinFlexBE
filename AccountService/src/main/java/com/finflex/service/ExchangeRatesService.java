package com.finflex.service;

import com.finflex.dto.request.GetExchangeRatesRequest;
import com.finflex.entitiy.ExchangeRates;
import com.finflex.repository.IExchangeRatesRepository;
import com.finflex.utility.ServiceManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRatesService  {


    private final IExchangeRatesRepository exchangeRatesRepository;
    private final RestTemplate restTemplate=new RestTemplate();
    @Value("${api.url}")
    private String apiUrl;
    private static final List<String> pairList=List.of("EUR/USD","USD/EUR","EUR/TRY","TRY/EUR","USD/TRY","TRY/USD"
    ,"CAD/TRY","TRY/CAD","EUR/CAD","CAD/EUR","CAD/USD","USD/CAD","CAD/GBP","GBP/CAD","GBP/EUR","EUR/GBP","GBP/USD","USD/GBP"
    ,"TRY/GBP","GBP/TRY");

    public ExchangeRatesService(IExchangeRatesRepository exchangeRatesRepository) {

        this.exchangeRatesRepository = exchangeRatesRepository;
    }

   // @PostConstruct
   // public void init() {
   //    getPairExchangeRates();
  //  }
    @Scheduled(cron = "0 0 * ? * *")
    public Boolean getPairExchangeRates() {
    for (String pair:pairList) {
        String url = apiUrl + pair;
        try {
            GetExchangeRatesRequest response = restTemplate.getForObject(url, GetExchangeRatesRequest.class);
            if(response != null && response.getResult().equals("success") && response.getConversion_rate() != null) {
                String currencyPair = response.getBase_code() + response.getTarget_code();
                Optional<ExchangeRates> optExc = exchangeRatesRepository.findOptionalByCurrencyPair(currencyPair);
                ExchangeRates exchangeRates;
                if (optExc.isPresent()) {
                    exchangeRates = optExc.get();
                    exchangeRates.setRate(response.getConversion_rate());
                } else {
                    exchangeRates = ExchangeRates.builder()
                            .currencyPair(currencyPair)
                            .rate(response.getConversion_rate())
                            .build();
                }
                exchangeRatesRepository.save(exchangeRates);
            }
        }catch (RestClientException e){
            System.err.println("Api request failed"+e.getMessage());
        }
    }
        return true;
    }

    public ExchangeRates getExchangeRates(String currencyPair){
        return exchangeRatesRepository.findByCurrencyPair(currencyPair);
    }

    public List<ExchangeRates> getAllExchangeRates() {
        return exchangeRatesRepository.findAll();
    }

}
