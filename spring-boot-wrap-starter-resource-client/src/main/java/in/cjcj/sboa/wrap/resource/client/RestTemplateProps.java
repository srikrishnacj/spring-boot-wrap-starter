package in.cjcj.sboa.wrap.resource.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("resource-client")
public class RestTemplateProps {
    private Map<String, Object> registrations;
}
