package in.cjcj.sboa.wrap.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@ConditionalOnProperty("caching.enable")
@Slf4j
public class CacheConfig {
    private final static String DEFAULT_CACHE_NAME = "default";
    private final static long DEFAULT_MAX_SIZE = 1000;

    private final CacheConfigProps cacheConfigProps;

    public CacheConfig(CacheConfigProps cacheConfigProps) {
        this.cacheConfigProps = cacheConfigProps;
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = new LinkedList<>();
        Map<String, CacheConfigProps.CacheInstanceConfig> configMap = this.cacheConfigProps.getConfigs();
        SimpleCacheManager manager = new SimpleCacheManager();
        if (configMap == null || configMap.isEmpty()) {
            log.error("Caching is enabled but cache properties are not found. " +
                    "So creating a default cache with name: {} and maxSize: {}. " +
                    "It is recommended to provide cache config explicitly", DEFAULT_CACHE_NAME, DEFAULT_MAX_SIZE);
            caches.add(buildDefaultCache());
        } else {
            for (String configName : configMap.keySet()) {
                CacheConfigProps.CacheInstanceConfig config = configMap.get(configName);
                CaffeineCache cache = buildCache(configName, config);
                caches.add(cache);
            }
        }
        manager.setCaches(caches);
        return manager;
    }

    private CaffeineCache buildCache(String configName, CacheConfigProps.CacheInstanceConfig config) {
        config.validate();

        Caffeine<Object, Object> builder = Caffeine
                .newBuilder()
                .ticker(ticker());
        if (config.getEviction().equals(CacheConfigProps.CacheEvictionType.MAXIMUM_SIZE)) {
            builder.maximumSize(config.getMaximumSize());
        } else if (config.getEviction().equals(CacheConfigProps.CacheEvictionType.EXPIRE_AFTER_ACCESS)) {
            builder.expireAfterAccess(config.getExpireAfterAccess(), TimeUnit.MINUTES);
        } else if (config.getEviction().equals(CacheConfigProps.CacheEvictionType.EXPIRE_AFTER_WRITE)) {
            builder.expireAfterWrite(config.getExpireAfterWrite(), TimeUnit.MINUTES);
        }
        return new CaffeineCache(configName, builder.build());
    }

    private CaffeineCache buildDefaultCache() {
        Caffeine<Object, Object> builder = Caffeine
                .newBuilder()
                .maximumSize(DEFAULT_MAX_SIZE)
                .ticker(ticker());
        return new CaffeineCache(DEFAULT_CACHE_NAME, builder.build());
    }
}
