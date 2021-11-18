package in.cjcj.sboa.wrap.rest.errors.codes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomErrorDetail implements ErrorDetail {
    private String code, message, developerMsg;

    public CustomErrorDetail(ErrorDetail errorDetail) {
        this.code = errorDetail.getCode();
        this.message = errorDetail.getMessage();
        this.developerMsg = errorDetail.getDeveloperMsg();
    }

    public CustomErrorDetail code(String code) {
        this.code = code;
        return this;
    }

    public CustomErrorDetail message(String message) {
        this.message = message;
        return this;
    }

    public CustomErrorDetail developerMsg(String developerMsg) {
        this.developerMsg = developerMsg;
        return this;
    }
}
