package in.cjcj.sboa.wrap.rest.errors.codes;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ErrorDetail {

    String getCode();

    String getMessage();

    String getDeveloperMsg();

    String toString();
}
