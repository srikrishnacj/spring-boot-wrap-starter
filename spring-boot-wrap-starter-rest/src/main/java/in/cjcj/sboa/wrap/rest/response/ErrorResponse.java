package in.cjcj.sboa.wrap.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends RestResponse {
    private ErrorDetail error;
    private List<ErrorDetail> subErrors = new LinkedList<>();

    private List<String> exception;
    private List<String> cause;
}
