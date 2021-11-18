package in.cjcj.sboa.wrap.resource.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
//@ConditionalOnProperty("resource-server.enable")
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    private final ResourceServerProps resourceServerProps;
    private final String contextPath;

    public ResourceServerConfig(ResourceServerProps resourceServerProps, Environment environment) {
        this.resourceServerProps = resourceServerProps;
        this.contextPath = environment.getProperty("server.servlet.context-path", "");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Disable CSRF Protection * Stateful session
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Allow all swagger api requests
        String[] WHITELIST_PATHS = this.appendContextPathToSwaggerPath(this.resourceServerProps.getWhitelistUrls(), contextPath);
        http.authorizeRequests().antMatchers(WHITELIST_PATHS).permitAll();

        // All other requests must be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // Enable OAuth2
        http
                .oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    private JwtDecoder jwtDecoder() {
        List<OAuth2TokenValidator<Jwt>> validators = new LinkedList<>();
        for (String claim : resourceServerProps.getVerifyClaims().keySet()) {
            String claimValue = resourceServerProps.getVerifyClaims().get(claim);

            OAuth2TokenValidator<Jwt> validator = new JwtClaimValidator<List>(claim, value -> {
                return value != null && value.contains(claimValue);
            });
        }

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<Jwt>(validators);

        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(resourceServerProps.getIssuerURI());
        decoder.setJwtValidator(validator);
        return decoder;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        List<Converter<Jwt, Collection<GrantedAuthority>>> converters = new LinkedList<>();
        for (String claim : resourceServerProps.getAuthorityMapping().keySet()) {
            String prefix = resourceServerProps.getAuthorityMapping().get(claim);

            JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
            converter.setAuthoritiesClaimName(claim);
            converter.setAuthorityPrefix(prefix);
        }
        Converter<Jwt, Collection<GrantedAuthority>> converter = new DelegatingJwtGrantedAuthoritiesConverter(converters);

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

    private String[] appendContextPathToSwaggerPath(List<String> AUTH_LIST, String contextPath) {
        List<String> collect = AUTH_LIST.stream().map(path -> path.startsWith("**") ? path : contextPath + path).collect(Collectors.toList());
        String[] paths = new String[collect.size()];
        for (int ii = 0; ii < collect.size(); ii++) {
            paths[ii] = collect.get(ii);
        }
        return paths;
    }
}
