package com.sstefanov.currency.rates.collector.service;

import com.sstefanov.currency.rates.collector.dto.CurrencyRatesDTO;
import com.sstefanov.currency.rates.collector.entities.CurrencyRate;
import com.sstefanov.currency.rates.collector.entities.CurrencyRateId;
import com.sstefanov.currency.rates.collector.repositories.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class CurrencyUpdater {

    private final CurrencyRateRepository currencyRateRepository;
    private final RestConnector restConnector;
    private final CacheRemover cacheRemover;

    @Scheduled(cron = "0 0 * * * *")
    public void runJob() {
        log.info("Starting up currency rates collector..");
        currencyRateRepository.saveAll(getCurrencyRates());
        cacheRemover.deleteCurrencyRateKeys();
    }

    private List<CurrencyRate> getCurrencyRates() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final List<CompletableFuture<List<CurrencyRate>>> futures = Arrays.stream(BaseCurrency.values())
                    .map(baseCurrency -> supplyAsync(() -> getRatesForBaseCurrency(baseCurrency), executor))
                    .toList();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Exception: ", e);
        }
    }

    private List<CurrencyRate> getRatesForBaseCurrency(BaseCurrency baseCurrency) {
        final CurrencyRatesDTO ratesDTO = restConnector.getRates(baseCurrency);
        final Map<String, BigDecimal> ratesMap = ratesDTO.getRates();
        final List<CurrencyRate> ratesEntities = new ArrayList<>();
        final Long sharpHour = getCurrentSharpHour();
        ratesMap.keySet().parallelStream().forEach(currency -> {
                    CurrencyRateId id = CurrencyRateId.builder()
                            .baseCurrency(ratesDTO.getBaseCurrency())
                            .timestamp(sharpHour)
                            .currency(currency)
                            .build();

                    CurrencyRate currencyRate = CurrencyRate.builder()
                            .id(id)
                            .rate(ratesMap.get(currency))
                            .build();
                    ratesEntities.add(currencyRate);
                }
        );
        return ratesEntities;
    }

    private Long getCurrentSharpHour() {
        return LocalDateTime.now(ZoneOffset.UTC)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
    }
}
