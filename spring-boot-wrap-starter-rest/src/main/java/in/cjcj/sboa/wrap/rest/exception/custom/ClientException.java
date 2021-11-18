package in.cjcj.sboa.wrap.rest.exception.custom;


import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.base.BadRequestException;

public class ClientException extends BadRequestException {
    public ClientException(AppErrors appErrors) {
        super(appErrors);
    }
    public ClientException(ErrorDetail errorDetail) {
        super(errorDetail);
    }
    public ClientException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }
}
