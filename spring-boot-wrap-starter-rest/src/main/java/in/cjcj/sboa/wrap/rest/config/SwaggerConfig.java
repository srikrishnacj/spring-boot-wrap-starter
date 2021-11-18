package in.cjcj.sboa.wrap.rest.config;

import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.util.AnnotationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(Environment environment) {
        String appPackage = environment.getProperty("wrap-rest-config.swagger.basePackage", "");
        if (!StringUtils.hasText(appPackage)) {
            appPackage = AnnotationUtil.findPackageNameForClassWithAnnotation(SpringBootApplication.class);
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(appPackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData(environment))
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, getResponses())
                .globalResponses(HttpMethod.POST, postResponses())
                .globalResponses(HttpMethod.PUT, putResponses())
                .globalResponses(HttpMethod.DELETE, deleteResponses());
    }

    private ApiInfo metaData(Environment environment) {
        return new ApiInfoBuilder()
                .title(environment.getProperty("info.app.name"))
                .description(environment.getProperty("info.app.description"))
                .version(environment.getProperty("info.app.version"))
                .build();
    }

    private List getResponses() {
        return allResponses(HttpStatus.OK,
                HttpStatus.ACCEPTED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.FORBIDDEN,
                HttpStatus.NOT_FOUND,
                HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.BAD_GATEWAY);
    }

    private List postResponses() {
        return allResponses(HttpStatus.OK,
                HttpStatus.CREATED,
                HttpStatus.ACCEPTED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.FORBIDDEN,
                HttpStatus.NOT_FOUND,
                HttpStatus.CONFLICT,
                HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.BAD_GATEWAY);
    }

    private List putResponses() {
        return allResponses(HttpStatus.OK,
                HttpStatus.ACCEPTED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.FORBIDDEN,
                HttpStatus.NOT_FOUND,
                HttpStatus.CONFLICT,
                HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.BAD_GATEWAY);
    }

    private List deleteResponses() {
        return allResponses(HttpStatus.OK,
                HttpStatus.ACCEPTED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.FORBIDDEN,
                HttpStatus.NOT_FOUND,
                HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.BAD_GATEWAY);
    }

    private List allResponses(HttpStatus... statusList) {
        Map<HttpStatus, Response> responseMap = new HashMap<>();
        responseMap.put(HttpStatus.OK,
                new ResponseBuilder()
                        .code("200")
                        .description("ok")
                        .build());
        responseMap.put(HttpStatus.CREATED,
                new ResponseBuilder()
                        .code("201")
                        .description("Resource Created")
                        .build());
        responseMap.put(HttpStatus.ACCEPTED,
                new ResponseBuilder()
                        .code("202")
                        .description("Request has been accepted and will not be processed immediately")
                        .build());
        responseMap.put(HttpStatus.MULTI_STATUS,
                new ResponseBuilder()
                        .code("207")
                        .description("When bulk operations are requested on more then one entity, if all operations are not succeed then this status code is returned")
                        .build());
        responseMap.put(HttpStatus.BAD_REQUEST,
                new ResponseBuilder()
                        .code("400")
                        .description(CommonResourceErrorCode.BAD_REQUEST.getMessage())
                        .build());
        responseMap.put(HttpStatus.UNAUTHORIZED,
                new ResponseBuilder()
                        .code("401")
                        .description(CommonResourceErrorCode.UNAUTHORIZED.getMessage())
                        .build());
        responseMap.put(HttpStatus.FORBIDDEN,
                new ResponseBuilder()
                        .code("401")
                        .description(CommonResourceErrorCode.FORBIDDEN.getMessage())
                        .build());
        responseMap.put(HttpStatus.NOT_FOUND,
                new ResponseBuilder()
                        .code("404")
                        .description(CommonResourceErrorCode.NOT_FOUND.getMessage())
                        .build());
        responseMap.put(HttpStatus.CONFLICT,
                new ResponseBuilder()
                        .code("409")
                        .description(CommonResourceErrorCode.ENTITY_OVERRIDE_ERROR.getMessage())
                        .build());
        responseMap.put(HttpStatus.UNPROCESSABLE_ENTITY,
                new ResponseBuilder()
                        .code("422")
                        .description(CommonResourceErrorCode.VALIDATION_ERROR.getMessage())
                        .build());
        responseMap.put(HttpStatus.INTERNAL_SERVER_ERROR,
                new ResponseBuilder()
                        .code("500")
                        .description(CommonResourceErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                        .build());
        responseMap.put(HttpStatus.BAD_GATEWAY,
                new ResponseBuilder()
                        .code("502")
                        .description(CommonResourceErrorCode.UPSTREAM_ERROR.getMessage())
                        .build());


        return Arrays.stream(statusList).map(status -> responseMap.get(status)).collect(Collectors.toList());
    }
}
