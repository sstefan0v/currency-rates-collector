package com.sstefanov.currency.rates.collector.service;

import com.sstefanov.currency.rates.collector.configs.Props;
import com.sstefanov.currency.rates.collector.dto.CurrencyRatesDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class RestConnector {

    private final String ACCESS_KEY = "access_key";
    private final String BASE_PARAM = "base";
    private final RestClient restClient;
    private final Props props;

    public RestConnector(Props props) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public CurrencyRatesDTO getRates(BaseCurrency baseCurrency) {
        log.debug("Get rates for base currency: {}", baseCurrency);
        ResponseEntity<CurrencyRatesDTO> response = restClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(props.getCurrenciesUrl())
                        .queryParam(ACCESS_KEY, props.getAccessKey())
                        .queryParam(BASE_PARAM, BaseCurrency.EUR) //hardcoded, since other currencies are not for free
                        .build())
                .retrieve().toEntity(CurrencyRatesDTO.class);
        return response.getBody();
    }
}
