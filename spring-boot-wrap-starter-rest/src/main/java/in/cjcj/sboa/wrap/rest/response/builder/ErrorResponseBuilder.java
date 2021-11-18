package in.cjcj.sboa.wrap.rest.response.builder;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.CustomErrorDetail;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.errors.codes.FormErrorDetail;
import in.cjcj.sboa.wrap.rest.response.ErrorResponse;
import in.cjcj.sboa.wrap.rest.response.RestResponse;
import in.cjcj.sboa.wrap.rest.util.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorResponseBuilder extends ResponseBuilder {
    private String traceId;
    private HttpStatus httpStatus;
    private AppErrors appErrors = new AppErrors();

    private ErrorResponseBuilder() {
        this.traceId = super.getTraceId();
    }

    public static ErrorResponseBuilder instance() {
        return new ErrorResponseBuilder();
    }

    public ErrorResponseBuilder badRequest() {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.appErrors.setError(CommonResourceErrorCode.BAD_REQUEST);
        return this;
    }

    public ErrorResponseBuilder notFound() {
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.appErrors.setError(CommonResourceErrorCode.NOT_FOUND);
        return this;
    }

    public ErrorResponseBuilder conflict() {
        this.httpStatus = HttpStatus.CONFLICT;
        this.appErrors.setError(CommonResourceErrorCode.ENTITY_OVERRIDE_ERROR);
        return this;
    }

    public ErrorResponseBuilder validationError() {
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        this.appErrors.setError(CommonResourceErrorCode.VALIDATION_ERROR);
        return this;
    }

    public ErrorResponseBuilder unauthorized() {
        this.httpStatus = HttpStatus.UNAUTHORIZED;
        this.appErrors.setError(CommonResourceErrorCode.UNAUTHORIZED);
        return this;
    }

    public ErrorResponseBuilder forbidden() {
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.appErrors.setError(CommonResourceErrorCode.FORBIDDEN);
        return this;
    }

    public ErrorResponseBuilder unknown() {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.appErrors.setError(CommonResourceErrorCode.INTERNAL_SERVER_ERROR);
        return this;
    }

    public ErrorResponseBuilder notImplemented() {
        this.httpStatus = HttpStatus.NOT_IMPLEMENTED;
        this.appErrors.setError(CommonResourceErrorCode.NOT_IMPLEMENTED);
        return this;
    }

    public ErrorResponseBuilder upstreamError() {
        this.httpStatus = HttpStatus.BAD_GATEWAY;
        this.appErrors.setError(CommonResourceErrorCode.UPSTREAM_ERROR);
        return this;
    }

    public ErrorResponseBuilder status(HttpStatus status) {
        Assert.notNull(status, "status should not be null");
        this.httpStatus = status;
        return this;
    }

    public ErrorResponseBuilder error(ErrorDetail error) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        this.appErrors.setError(error);
        return this;
    }

    public ErrorResponseBuilder error(ErrorDetail error, String developerMsg) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");
        ErrorBuilder errorBuilder = new ErrorBuilder();

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.setError(error);
        return this;
    }

    public ErrorResponseBuilder subError(ErrorDetail errorDetail) {
        Assert.notNull(errorDetail, "ErrorDetail should not be null for ErrorResponse");
        this.appErrors.getSubErrors().add(errorDetail);
        return this;
    }

    public ErrorResponseBuilder subError(ErrorDetail error, String developerMsg) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.getSubErrors().add(error);
        return this;
    }

    public ErrorResponseBuilder fieldError(String field, ErrorDetail error) {
        Assert.hasText(field, "field name should not be null");
        Assert.notNull(error, "error detail for a field should not be null for ErrorResponse");
        this.appErrors.getSubErrors().add(new FormErrorDetail(field, error));
        return this;
    }

    public ErrorResponseBuilder fieldError(String field, ErrorDetail error, String developerMsg) {
        Assert.hasText(field, "field name should not be null");
        Assert.notNull(error, "error detail for a field should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.getSubErrors().add(new FormErrorDetail(field, error));
        return this;
    }

    public ErrorResponseBuilder exception(Throwable exception) {
        Assert.notNull(exception, "exception should not be null for ErrorResponse");
        this.appErrors.setException(exception);
        return this;
    }

    public ErrorResponseBuilder fromAppError(AppErrors appErrors) {
        Assert.notNull(appErrors, "AppErrors object should not be null");
        this.appErrors = appErrors;
        return this;
    }

    @Override
    protected RestResponse response(boolean enableDebugInfo) {
        Assert.notNull(this.httpStatus, "Http Status Code Should not be null");
        Assert.notNull(this.appErrors.getError(), "Error Code Should not be null");

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(System.currentTimeMillis());
        response.setRequestId(super.getTraceId());
        response.setStatus(this.httpStatus.value());

        response.setError(debugConfigAwareErrorDetail(this.appErrors.getError(), enableDebugInfo));
        response.setSubErrors(debugConfigAwareSubErrors(this.appErrors.getSubErrors(), enableDebugInfo));
        response.setException(debugConfigAwareException(enableDebugInfo));
        response.setCause(debugConfigAwareCause(enableDebugInfo));

        return response;
    }

    private ErrorDetail debugConfigAwareErrorDetail(ErrorDetail errorDetail, boolean isDebugEnabled) {
        if (!isDebugEnabled && StringUtils.hasText(errorDetail.getDeveloperMsg())) {
            return new CustomErrorDetail(errorDetail).developerMsg(null);
        } else {
            return errorDetail;
        }
    }

    private List<ErrorDetail> debugConfigAwareSubErrors(List<ErrorDetail> errorDetails, boolean isDebugEnabled) {
        if (!isDebugEnabled) {
            return errorDetails.stream().map(el -> this.debugConfigAwareErrorDetail(el, isDebugEnabled)).collect(Collectors.toList());
        } else {
            return errorDetails;
        }
    }

    private List<String> debugConfigAwareException(boolean isDebugEnabled) {
        if (isDebugEnabled && this.appErrors.getException() != null) {
            return ExceptionUtil.formatToPrettyString(this.appErrors.getException());
        } else {
            return null;
        }
    }

    private List<String> debugConfigAwareCause(boolean isDebugEnabled) {
        if (isDebugEnabled && this.appErrors.getException() != null && this.appErrors.getException().getCause() != null) {
            return ExceptionUtil.formatToPrettyString(this.appErrors.getException().getCause());
        } else {
            return null;
        }
    }
}
