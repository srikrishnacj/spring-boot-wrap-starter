package in.cjcj.sboa.wrap.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OkResponse extends RestResponse {
    private Object data;
    private boolean isCollection; // if -1 then data is object or null. if 0 or > 0 then data is collection
    private Pagination pageable;
}
