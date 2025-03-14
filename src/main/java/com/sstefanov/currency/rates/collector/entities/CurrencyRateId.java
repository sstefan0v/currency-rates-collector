package com.sstefanov.currency.rates.collector.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CurrencyRateId implements Serializable {

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = false, length = 3)
    private String baseCurrency;

    @Column(nullable = false, length = 3)
    private String currency;
}
