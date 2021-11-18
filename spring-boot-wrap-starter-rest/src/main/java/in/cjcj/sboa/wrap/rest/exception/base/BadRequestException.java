package in.cjcj.sboa.wrap.rest.exception.base;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = CommonResourceErrorCode.Fields.BAD_REQUEST)
public class BadRequestException extends AppException {
    public BadRequestException(AppErrors appErrors) {
        super(appErrors);
    }

    public BadRequestException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

    public BadRequestException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }

}
