package in.cjcj.sboa.wrap.rest.errors.codes;

import lombok.Getter;

@Getter
public class FormErrorDetail implements ErrorDetail {
    private final String field, code, message, developerMsg;

    public FormErrorDetail(String field, ErrorDetail errorDetail) {
        this.field = field;
        this.code = errorDetail.getCode();
        this.message = errorDetail.getMessage();
        this.developerMsg = errorDetail.getMessage();
    }
}
