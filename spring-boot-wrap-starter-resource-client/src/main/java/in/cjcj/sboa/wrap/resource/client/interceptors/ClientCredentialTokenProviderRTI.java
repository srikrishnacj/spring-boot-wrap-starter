package in.cjcj.sboa.wrap.resource.client.interceptors;

import in.cjcj.sboa.wrap.resource.client.config.StaticReferences;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Instant;

/*
    due to some bugs in spring security, we are manually creating the token and caching it and validating it
    https://stackoverflow.com/a/60017312
 */
public class ClientCredentialTokenProviderRTI implements ClientHttpRequestInterceptor {
    private final String registrationId;
    private OAuth2AccessToken accessToken;

    public ClientCredentialTokenProviderRTI(String registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + getToken().getTokenValue());
        return execution.execute(request, body);
    }

    private OAuth2AccessToken getToken() {
        if (!this.isValidAccessToken()) {
            this.createAccessToken();
        }
        return this.accessToken;
    }

    private boolean isValidAccessToken() {
        if (this.accessToken == null) return false;
        if (this.accessToken.getExpiresAt().isBefore(Instant.now())) return false;
        return true;
    }

    private void createAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(this.registrationId)
                .principal(this.registrationId)
                .build();
        ApplicationContext applicationContext = StaticReferences.applicationContext;
        OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager = StaticReferences.getBean(OAuth2AuthorizedClientManager.class);
        Assert.notNull(oAuth2AuthorizedClientManager, "OAuth2AuthorizedClientManager should not be null. " +
                "one possible reason this being null could be there is no client registration configured in properties");
        OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientManager.authorize(authorizeRequest);
        Assert.notNull(authorizedClient, "OAuth2AuthorizedClient client should not be null. " +
                "one possible reason could be for the given registration id: " + registrationId + " there may not be registration configuration provided in config files");
        this.accessToken = authorizedClient.getAccessToken();
    }
}
