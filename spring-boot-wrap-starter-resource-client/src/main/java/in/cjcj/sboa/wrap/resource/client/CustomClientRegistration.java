package in.cjcj.sboa.wrap.resource.client;

import lombok.Data;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.Set;

@Data
public class CustomClientRegistration {
    private String registrationId;
    private String clientId;
    private String clientSecret;
    private Set<String> scopes = Collections.emptySet();
    private String tokenUri;

    public ClientRegistration clientRegistration() {
        return ClientRegistration
                .withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(scopes)
                .tokenUri(tokenUri)
                .build();
    }
}
