package in.cjcj.sboa.wrap.resource.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("resource-server")
public class ResourceServerProps {
    private boolean enable;
    private String issuerURI;
    private Map<String, String> verifyClaims;
    private Map<String, String> authorityMapping;
    private List<String> whitelistUrls;
}
