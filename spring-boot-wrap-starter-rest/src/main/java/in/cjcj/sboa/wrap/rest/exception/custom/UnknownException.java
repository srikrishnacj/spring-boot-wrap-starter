package in.cjcj.sboa.wrap.rest.exception.custom;


import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.base.InternalServerException;

public class UnknownException extends InternalServerException {
    public UnknownException(AppErrors appErrors) {
        super(appErrors);
    }

    public UnknownException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

    public UnknownException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }
}