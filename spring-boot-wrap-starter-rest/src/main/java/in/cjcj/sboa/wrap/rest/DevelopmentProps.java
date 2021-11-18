package in.cjcj.sboa.wrap.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties("wrap-rest-config")
public class DevelopmentProps {
    private Logging logging;
    private Controller controller;
    private Response response;

    @Data
    public static class Controller {
        private boolean enableErrorCodesCtrl;
    }

    @Data
    public static class Logging {
        private Servlet servlet;
        private RestTemplate restTemplate;
        private boolean springWebDebug;

        public static enum LoggingOptions {
            NONE, BASIC, QUERY_PARAMS, HEADER, BODY
        }

        @Data
        public static class Servlet {
            private Set<LoggingOptions> request;
            private Set<LoggingOptions> response;
        }

        @Data
        public static class RestTemplate {
            private Set<LoggingOptions> request;
            private Set<LoggingOptions> response;
        }
    }

    @Data
    public static class Response {
        private boolean includeDebugInfo = false;
        private boolean enableGlobalCors = false;
    }
}
