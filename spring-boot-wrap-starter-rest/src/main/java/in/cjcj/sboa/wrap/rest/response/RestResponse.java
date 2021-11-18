package in.cjcj.sboa.wrap.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class RestResponse {
    private long timestamp = System.currentTimeMillis();
    private String requestId;
    private int status;
}