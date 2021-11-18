package in.cjcj.sboa.wrap.rest.handler;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorDetailResolver;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.AppException;
import in.cjcj.sboa.wrap.rest.response.builder.ErrorResponseBuilder;
import in.cjcj.sboa.wrap.rest.response.builder.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({AppException.class})
    public final ResponseEntity handleHandleAppException(AppException ex, WebRequest request) {
        log.error("application exception", ex);
        AppErrors appErrors = ex.getAppErrors();
        appErrors.validate();

        ResponseStatus annotation = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        ErrorResponseBuilder builder = ResponseBuilder.errorResponse().fromAppError(appErrors);

        // set http status code
        if (annotation == null) {
            builder.status(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            builder.status(annotation.value());
        }

        // set error
        if (appErrors.getError() == null && annotation != null) {
            String errorMessage = annotation.reason();
            ErrorDetail errorDetail = ErrorDetailResolver.resolve(errorMessage);
            builder.error(errorDetail);
        } else if (appErrors.getError() == null && annotation == null) {
            builder.error(CommonResourceErrorCode.INTERNAL_SERVER_ERROR);
        }

        return builder.build();
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity handleUnknownException(Exception ex, WebRequest request) {
        log.error("unknown exception", ex);

        ErrorResponseBuilder responseBuilder = ResponseBuilder
                .errorResponse()
                .unknown()
                .exception(ex);

        return responseBuilder.build();
    }
}
