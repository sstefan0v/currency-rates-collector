package com.sstefanov.currency.rates.collector.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrencyRatesDTO {

    private long timestamp;

    @JsonProperty("base")
    private String baseCurrency;

    private LocalDate date;
    private Map<String, BigDecimal> rates;
}