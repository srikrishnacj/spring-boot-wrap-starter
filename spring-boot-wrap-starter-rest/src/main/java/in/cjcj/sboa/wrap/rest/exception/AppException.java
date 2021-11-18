package in.cjcj.sboa.wrap.rest.exception;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = CommonResourceErrorCode.Fields.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
    @Getter
    private final AppErrors appErrors;

    public AppException() {
        this(new AppErrors());
    }

    public AppException(ErrorDetail errorDetail) {
        this(ErrorBuilder.withError(errorDetail).build());
    }

    public AppException(ErrorBuilder errorBuilder) {
        this(errorBuilder.build());
    }

    public AppException(AppErrors appErrors) {
        super(""); // to avoid print error message as null on console. printing null sometime confuses us as null pointer exception
        this.appErrors = appErrors;
        if (appErrors.getException() == null) appErrors.setException(this);
    }
}