package in.cjcj.sboa.wrap.rest.response.builder;

import in.cjcj.sboa.wrap.rest.DevelopmentProps;
import in.cjcj.sboa.wrap.rest.config.StaticReferences;
import in.cjcj.sboa.wrap.rest.response.RestResponse;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public abstract class ResponseBuilder {

    public static OkResponseBuilder okResponse() {
        return OkResponseBuilder.instance();
    }

    public static ErrorResponseBuilder errorResponse() {
        return ErrorResponseBuilder.instance();
    }

    protected String getTraceId() {
        Span span = StaticReferences.getBean(Tracer.class).currentSpan();
        TraceContext context = null;
        String requestId = null;

        if (span != null) {
            context = span.context();
        }
        if (context != null) {
            requestId = context.traceId();
        }
        return requestId;
    }

    protected abstract RestResponse response(boolean enableDebugInfo);

    public ResponseEntity build() {
        DevelopmentProps bean = StaticReferences.getBean(DevelopmentProps.class);
        boolean includeDebugInfo = bean.getResponse().isIncludeDebugInfo();
        RestResponse response = this.response(includeDebugInfo);

        Assert.notNull(response.getStatus(), "HttpStatus code should not be null");

        try {
            HttpStatus.valueOf(response.getStatus());
        } catch (IllegalArgumentException ex) {
            Assert.isTrue(false, "Given status code is not valid. please only use status codes defined in Spring HttpStatus class");
        }

        ResponseEntity<Object> responseEntity = ResponseEntity
                .status(response.getStatus())
                .body(response);
        return responseEntity;
    }
}