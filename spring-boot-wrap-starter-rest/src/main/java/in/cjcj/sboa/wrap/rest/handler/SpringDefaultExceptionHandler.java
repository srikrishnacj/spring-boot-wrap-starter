package in.cjcj.sboa.wrap.rest.handler;

import in.cjcj.sboa.wrap.rest.errors.ErrorDetailResolver;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.CustomErrorDetail;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.response.builder.ErrorResponseBuilder;
import in.cjcj.sboa.wrap.rest.response.builder.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class SpringDefaultExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
//    protected ResponseEntity handleObjectOptimisticLockingFailure(ObjectOptimisticLockingFailureException exception) {
//        return ResponseBuilder
//                .errorResponse()
//                .conflict()
//                .exception(exception)
//                .build();
//    }

    @ExceptionHandler({HttpClientErrorException.class})
    protected ResponseEntity handleHttpClientErrorException(HttpClientErrorException exception) {
        return ResponseBuilder
                .errorResponse()
                .unknown()
                .exception(exception)
                .build();
    }

    @ExceptionHandler({HttpServerErrorException.class})
    protected ResponseEntity handleHttpServerErrorException(HttpServerErrorException exception) {
        return ResponseBuilder
                .errorResponse()
                .upstreamError()
                .exception(exception)
                .build();
    }

    @ExceptionHandler({UnknownHttpStatusCodeException.class})
    protected ResponseEntity handleUnknownHttpStatusCodeException(UnknownHttpStatusCodeException exception) {
        return ResponseBuilder
                .errorResponse()
                .upstreamError()
                .exception(exception)
                .build();
    }

    @Override
    protected ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String supportedMethods = "";
        if (supportedMethods != null) {
            supportedMethods = StringUtils.arrayToDelimitedString(ex.getSupportedMethods(), ", ");
        }

        String msg = ex.getMethod() + " HTTP Method not allowed on this endpoint. Supported Endpoints are: " + supportedMethods;
        CustomErrorDetail errorDetail = new CustomErrorDetail(CommonResourceErrorCode.METHOD_NOT_ALLOWED).developerMsg(msg);

        return ResponseBuilder
                .errorResponse()
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .error(errorDetail)
                .exception(ex)
                .build();
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponseBuilder responseBuilder = ResponseBuilder.errorResponse().validationError();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            try {
                String errorMessage = error.getDefaultMessage();
                ErrorDetail errorDetail = ErrorDetailResolver.resolve(errorMessage);
                responseBuilder.fieldError(fieldName, errorDetail);
            } catch (IllegalArgumentException iae) {
                fieldName = ex.getBindingResult().getTarget().getClass().getSimpleName() + "." + fieldName;
                String devMsg = "Error while resolving error detail for field: " + fieldName +
                        ". Possible cause could be you might not provided custom error code to validation annotations";

                log.error(devMsg, fieldName, iae);
                CustomErrorDetail errorDetail = new CustomErrorDetail(CommonResourceErrorCode.INTERNAL_SERVER_ERROR).developerMsg(devMsg);
                return ResponseBuilder.errorResponse()
                        .unknown()
                        .error(errorDetail)
                        .exception(iae)
                        .build();
            }
        }
        return responseBuilder.build();
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseBuilder
                .errorResponse()
                .validationError()
                .exception(ex)
                .fieldError(ex.getParameterName(), CommonResourceErrorCode.PARAMETER_MISSING)
                .build();
    }
}