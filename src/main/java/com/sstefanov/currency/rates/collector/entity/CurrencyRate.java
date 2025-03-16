package com.sstefanov.currency.rates.collector.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "currency_rates")
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {

    @EmbeddedId
    private CurrencyRateId id;

    @Column(nullable = false, precision = 20, scale = 10)
    private BigDecimal rate;
}
