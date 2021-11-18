package in.cjcj.sboa.wrap.rest.exception.base;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED, reason = CommonResourceErrorCode.Fields.NOT_IMPLEMENTED)
public class NotImplementedException extends AppException {
    public NotImplementedException(AppErrors appErrors) {
        super(appErrors);
    }
    public NotImplementedException(ErrorDetail errorDetail) {
        super(errorDetail);
    }
    public NotImplementedException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }
}
