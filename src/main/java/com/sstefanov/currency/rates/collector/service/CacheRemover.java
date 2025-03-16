package com.sstefanov.currency.rates.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static org.springframework.data.redis.core.ScanOptions.scanOptions;

import org.springframework.data.redis.core.RedisCallback;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheRemover {
    private static final String KEY_PATTERN = "rates_*";
    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteCurrencyRateKeys() {
        Set<String> keysToDelete = new HashSet<>();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            RedisKeyCommands keyCommands = connection.keyCommands();
            try (Cursor<byte[] >cursor = keyCommands.scan(scanOptions()
                    .match(KEY_PATTERN)
                    .count(1000)
                    .build())) {
                cursor.forEachRemaining(key -> keysToDelete.add(new String(key, StandardCharsets.UTF_8)));
            }
            return null;
        });

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
            log.info("Deleted {} keys from cache.", keysToDelete.size());
            log.debug("Deleted keys from cache: {}", keysToDelete);
        } else {
            log.info("No keys to be deleted from cache.");
        }
    }
}
