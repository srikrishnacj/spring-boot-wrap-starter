package in.cjcj.sboa.wrap.resource.client;

import in.cjcj.sboa.wrap.resource.client.config.RestTemplateConfig;
import in.cjcj.sboa.wrap.resource.client.config.StaticReferences;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RestTemplateProps.class)
@Import({RestTemplateConfig.class, StaticReferences.class})
public class WrapResourceClientAutoConfiguration {
}
