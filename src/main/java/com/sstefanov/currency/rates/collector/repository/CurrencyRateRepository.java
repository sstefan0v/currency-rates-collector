package com.sstefanov.currency.rates.collector.repository;

import com.sstefanov.currency.rates.collector.entity.CurrencyRate;
import com.sstefanov.currency.rates.collector.entity.CurrencyRateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, CurrencyRateId> {

}