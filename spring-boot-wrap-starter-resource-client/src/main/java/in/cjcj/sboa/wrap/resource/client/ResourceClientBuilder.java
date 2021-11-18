package in.cjcj.sboa.wrap.resource.client;

import in.cjcj.sboa.wrap.resource.client.interceptors.ClientCredentialTokenProviderRTI;
import in.cjcj.sboa.wrap.resource.client.interceptors.CustomHeaderProviderRTI;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class ResourceClientBuilder {
    private static RestTemplateBuilder GLOBAL_BUILDER;

    private final RestTemplateBuilder builder;

    public static void init(RestTemplateBuilder builder) {
        GLOBAL_BUILDER = builder;
    }

    private ResourceClientBuilder(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    public static ResourceClientBuilder builder() {
        return new ResourceClientBuilder(GLOBAL_BUILDER);
    }

    public ResourceClientBuilder withClientCredentialFlow(String registrationId) {
        ClientCredentialTokenProviderRTI interceptor = new ClientCredentialTokenProviderRTI(registrationId);
        this.builder.additionalInterceptors(interceptor);
        return this;
    }

    public ResourceClientBuilder withCustomHeaderInjector(String header, String value) {
        CustomHeaderProviderRTI interceptor = new CustomHeaderProviderRTI(header, value);
        this.builder.additionalInterceptors(interceptor);
        return this;
    }

    public ResourceClientBuilder enableLogging() {
        // TODO will be implemented in future
        return this;
    }

    public RestTemplate build() {
        return this.builder.build();
    }
}
