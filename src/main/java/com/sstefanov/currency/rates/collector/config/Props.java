package com.sstefanov.currency.rates.collector.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fixer.io")
public class Props {
    private String baseUrl;
    private String currenciesUrl;
    private String accessKey;
    private String baseCurrency;
}
