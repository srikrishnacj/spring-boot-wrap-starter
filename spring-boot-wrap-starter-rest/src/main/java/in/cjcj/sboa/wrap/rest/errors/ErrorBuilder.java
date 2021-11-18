package in.cjcj.sboa.wrap.rest.errors;

import in.cjcj.sboa.wrap.rest.errors.codes.CustomErrorDetail;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.errors.codes.FormErrorDetail;
import org.springframework.util.Assert;

public class ErrorBuilder {
    private AppErrors appErrors = new AppErrors();

    public static ErrorBuilder withError(ErrorDetail error) {
        ErrorBuilder errorBuilder = new ErrorBuilder();
        errorBuilder.appErrors.setError(error);
        return errorBuilder;
    }

    public static ErrorBuilder withError(ErrorDetail error, String developerMsg) {
        ErrorBuilder errorBuilder = new ErrorBuilder();
        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        errorBuilder.appErrors.setError(error);
        return errorBuilder;
    }

    public static ErrorBuilder withSubError(ErrorDetail error) {
        ErrorBuilder errorBuilder = new ErrorBuilder();
        errorBuilder.subError(error);
        return errorBuilder;
    }

    public static ErrorBuilder withSubError(ErrorDetail error, String developerMsg) {
        ErrorBuilder errorBuilder = new ErrorBuilder();
        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        errorBuilder.subError(error);
        return errorBuilder;
    }

    public ErrorBuilder error(ErrorDetail error) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        this.appErrors.setError(error);
        return this;
    }

    public ErrorBuilder error(ErrorDetail error, String developerMsg) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.setError(error);
        return this;
    }

    public ErrorBuilder subError(ErrorDetail errorDetail) {
        Assert.notNull(errorDetail, "ErrorDetail should not be null for ErrorResponse");
        this.appErrors.getSubErrors().add(errorDetail);
        return this;
    }

    public ErrorBuilder subError(ErrorDetail error, String developerMsg) {
        Assert.notNull(error, "ErrorDetail should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.getSubErrors().add(error);
        return this;
    }

    public ErrorBuilder fieldError(String field, ErrorDetail error) {
        Assert.hasText(field, "field name should not be null");
        Assert.notNull(error, "error detail for a field should not be null for ErrorResponse");
        this.appErrors.getSubErrors().add(new FormErrorDetail(field, error));
        return this;
    }

    public ErrorBuilder fieldError(String field, ErrorDetail error, String developerMsg) {
        Assert.hasText(field, "field name should not be null");
        Assert.notNull(error, "error detail for a field should not be null for ErrorResponse");
        Assert.hasText(developerMsg, "developerMsg name should not be empty or null");

        error = new CustomErrorDetail(error).developerMsg(developerMsg);
        this.appErrors.getSubErrors().add(new FormErrorDetail(field, error));
        return this;
    }

    public ErrorBuilder exception(Exception exception) {
        Assert.notNull(exception, "exception should not be null");
        this.appErrors.setException(exception);
        return this;
    }

    public boolean hasErrors() {
        return this.appErrors.hasErrors();
    }

    public AppErrors build() {
        this.appErrors.validate();
        return this.appErrors;
    }
}