package in.cjcj.sboa.wrap.rest;

import in.cjcj.sboa.wrap.rest.config.StaticReferences;
import in.cjcj.sboa.wrap.rest.config.WebMvcConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"in.cjcj.sboa.wrap.rest.web", "in.cjcj.sboa.wrap.rest.handler"})
@EnableConfigurationProperties(DevelopmentProps.class)
@Import({WebMvcConfig.class, StaticReferences.class})
public class WrapRestAutoConfiguration {
    private final DevelopmentProps developmentProps;

    public WrapRestAutoConfiguration(DevelopmentProps developmentProps) {
        this.developmentProps = developmentProps;
    }
}
