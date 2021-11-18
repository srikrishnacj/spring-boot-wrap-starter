package in.cjcj.sboa.wrap.rest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import in.cjcj.sboa.wrap.rest.DevelopmentProps;
import in.cjcj.sboa.wrap.rest.filters.CustomServletRequestLoggingFilter;
import in.cjcj.sboa.wrap.rest.filters.NoOpServletRequestLoggingFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final DevelopmentProps developmentProps;

    public WebMvcConfig(DevelopmentProps developmentProps) {
        this.developmentProps = developmentProps;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (this.developmentProps.getResponse().isEnableGlobalCors()) {
            registry.addMapping("/**");
        }
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .failOnUnknownProperties(false)
                .featuresToDisable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        Set<DevelopmentProps.Logging.LoggingOptions> request = this.developmentProps.getLogging().getServlet().getRequest();
        Set<DevelopmentProps.Logging.LoggingOptions> response = this.developmentProps.getLogging().getServlet().getResponse();

        if (request == null || request.isEmpty() || request.contains(DevelopmentProps.Logging.LoggingOptions.NONE)) {
            return new NoOpServletRequestLoggingFilter();
        }

        if (response == null || response.isEmpty() || response.contains(DevelopmentProps.Logging.LoggingOptions.NONE)) {
            return new NoOpServletRequestLoggingFilter();
        }

        return new CustomServletRequestLoggingFilter(this.developmentProps.getLogging().getServlet());
    }
}