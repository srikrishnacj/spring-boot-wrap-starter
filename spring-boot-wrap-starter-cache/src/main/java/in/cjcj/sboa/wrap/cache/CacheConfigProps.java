package in.cjcj.sboa.wrap.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Map;

@Configuration
@ConfigurationProperties("caching")
@Getter
@Setter
public class CacheConfigProps {
    private Map<String, CacheInstanceConfig> configs;

    public static enum CacheEvictionType {
        MAXIMUM_SIZE, EXPIRE_AFTER_ACCESS, EXPIRE_AFTER_WRITE
    }

    @Getter
    @Setter
    public static class CacheInstanceConfig {
        private CacheEvictionType eviction;
        private int maximumSize;
        private int expireAfterAccess;
        private int expireAfterWrite;

        public void validate() {
            Assert.notNull(this.eviction, "eviction type should not be null");
        }
    }

}
