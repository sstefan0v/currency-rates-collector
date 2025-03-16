package com.sstefanov.currency.rates.collector.repositories;

import com.sstefanov.currency.rates.collector.entities.CurrencyRate;
import com.sstefanov.currency.rates.collector.entities.CurrencyRateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, CurrencyRateId> {

}