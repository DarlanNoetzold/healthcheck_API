package tech.noetzold.healthcheckgate.cache;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put("metric", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1)));
            configurationMap.put("accuracy", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(20)));

            builder.withInitialCacheConfigurations(configurationMap);
        };
    }

}

