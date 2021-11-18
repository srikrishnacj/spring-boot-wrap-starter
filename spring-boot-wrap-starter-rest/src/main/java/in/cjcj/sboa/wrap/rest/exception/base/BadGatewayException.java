package in.cjcj.sboa.wrap.rest.exception.base;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY, reason = CommonResourceErrorCode.Fields.UPSTREAM_ERROR)
public class BadGatewayException extends AppException {
    public BadGatewayException(AppErrors appErrors) {
        super(appErrors);
    }

    public BadGatewayException(ErrorDetail errorDetail) {
        super(errorDetail);
    }

    public BadGatewayException(ErrorBuilder errorBuilder) {
        super(errorBuilder);
    }
}
