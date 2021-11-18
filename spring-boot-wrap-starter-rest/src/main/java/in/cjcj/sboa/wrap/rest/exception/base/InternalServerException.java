package in.cjcj.sboa.wrap.rest.exception.base;


import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.AppException;

public class InternalServerException extends AppException {
    public InternalServerException(AppErrors appErrors) {
        super(appErrors);
    }
    public InternalServerException(ErrorDetail errorDetail) {
        super(errorDetail);
    }
    public InternalServerException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }
}
