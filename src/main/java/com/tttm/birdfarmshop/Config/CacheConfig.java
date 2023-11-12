package com.tttm.birdfarmshop.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;
@Configuration
@EnableCaching
@EnableTransactionManagement
public class CacheConfig{
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "user", "users",
                "food", "foods",
                "nest", "nests",
                "bird", "birds",
                "voucher", "vouchers",
                "typeOfBird", "typeOfBirds"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(50)
                .weakKeys()
                .recordStats()
                .expireAfterAccess(30, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
