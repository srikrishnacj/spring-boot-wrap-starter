package in.cjcj.sboa.wrap.rest.errors.codes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.Assert;

/**
 * References
 * https://stackoverflow.com/a/68471539
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum CommonResourceErrorCode implements ErrorDetail {
    @FieldNameConstants.Include BAD_REQUEST("bad request"),
    @FieldNameConstants.Include NOT_FOUND("resource you are trying to get or trying to perform actions does not exits"),
    @FieldNameConstants.Include VALIDATION_ERROR("validation error. possibly request params or request body missing required values or contains invalid values"),
    @FieldNameConstants.Include ENTITY_OVERRIDE_ERROR("resource you are trying to update is already modified. you have older version of resource. please update to new version and try again"),
    @FieldNameConstants.Include UNAUTHORIZED("no valid token received or unable to properly authenticate request "),
    @FieldNameConstants.Include FORBIDDEN("user or system does not have enough privileges to access the protected resource or the endpoint"),
    @FieldNameConstants.Include NOT_IMPLEMENTED("this feature or endpoint or operation is not yet implemented"),
    @FieldNameConstants.Include UPSTREAM_ERROR("unable to connect with upstream service or upstream service failed to respond properly"),
    @FieldNameConstants.Include INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Something went wrong"),
    @FieldNameConstants.Include METHOD_NOT_ALLOWED("Requested HTTP Method not supported"),


    @FieldNameConstants.Include PARAMETER_MISSING("request parameter value is missing"),
    @FieldNameConstants.Include RESOURCE_NOT_FOUND("resource not found"),
    @FieldNameConstants.Include ID_SHOULD_NOT_BE_BLANK("id should not be null"),

    @FieldNameConstants.Include BEAN_ID_COPY_ERROR("ERROR_101", "something went wrong", "Something went wrong while copying bean properties from one bean to another bean");


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
