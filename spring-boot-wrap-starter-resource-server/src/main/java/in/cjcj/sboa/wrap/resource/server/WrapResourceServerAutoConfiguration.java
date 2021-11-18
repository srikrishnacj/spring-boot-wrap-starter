package in.cjcj.sboa.wrap.resource.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(ResourceServerProps.class)
@Import({ResourceServerConfig.class})
@ConditionalOnWebApplication
public class WrapResourceServerAutoConfiguration {

}
