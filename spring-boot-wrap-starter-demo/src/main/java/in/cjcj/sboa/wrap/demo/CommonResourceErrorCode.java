package in.cjcj.sboa.wrap.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.Assert;

/**
 * References
 * https://stackoverflow.com/a/68471539
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum CommonResourceErrorCode implements ErrorDetail {
    @FieldNameConstants.Include BAD_REQUEST2("bad request");


    private String code, message, developerMsg;

    CommonResourceErrorCode(String message) {
        Assert.hasText(message, "message should not be empty");
        this.code = name();
        this.message = message;
    }

    /**
     * You can pass custom code as integer values if you don't want code to be enum name
     */
    CommonResourceErrorCode(String code, String message) {
        Assert.hasText(code, "code should not be empty");
        Assert.hasText(message, "message should not be empty");

        this.code = code;
        this.message = message;
    }

    CommonResourceErrorCode(String code, String message, String developerMsg) {
        Assert.hasText(code, "code should not be empty");
        Assert.hasText(message, "message should not be empty");
        Assert.hasText(developerMsg, "developerMsg should not be empty");

        this.code = code;
        this.message = message;
        this.developerMsg = developerMsg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDeveloperMsg() {
        return developerMsg;
    }
}
