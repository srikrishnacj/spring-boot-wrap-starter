package in.cjcj.sboa.wrap.cache;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@ComponentScan({"in.cjcj.sboa.wrap.cache"})
@EnableConfigurationProperties(CacheConfigProps.class)
@Import({CacheConfig.class})
public class WarpCacheAutoConfiguration {

}
