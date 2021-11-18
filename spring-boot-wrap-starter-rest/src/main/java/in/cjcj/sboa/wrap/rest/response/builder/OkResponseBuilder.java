package in.cjcj.sboa.wrap.rest.response.builder;

import in.cjcj.sboa.wrap.rest.response.OkResponse;
import in.cjcj.sboa.wrap.rest.response.Pagination;
import in.cjcj.sboa.wrap.rest.response.RestResponse;
import in.cjcj.sboa.wrap.rest.util.JsonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.Collection;

public class OkResponseBuilder extends ResponseBuilder {
    private final OkResponse response;

    private OkResponseBuilder() {
        this.response = new OkResponse();
        this.response.setRequestId(this.getTraceId());
    }

    public static OkResponseBuilder instance() {
        return new OkResponseBuilder();
    }

    public OkResponseBuilder ok() {
        this.response.setStatus(HttpStatus.OK.value());
        return this;
    }

    public OkResponseBuilder created() {
        this.response.setStatus(HttpStatus.CREATED.value());
        return this;
    }

    public OkResponseBuilder accepted() {
        this.response.setStatus(HttpStatus.ACCEPTED.value());
        return this;
    }

    public OkResponseBuilder noContent() {
        this.response.setStatus(HttpStatus.NO_CONTENT.value());
        return this;
    }

    public OkResponseBuilder status(HttpStatus status) {
        this.response.setStatus(status.value());
        return this;
    }

    public OkResponseBuilder data(Object data) {
        Assert.notNull(data, "data should not be null for OkResponse");
        this.response.setData(data);

        if (data instanceof Collection) {
            this.response.setCollection(true);
        } else if (data instanceof Page) {
            this.response.setCollection(true);
            Page page = (Page) data;
            Pageable pageable = page.getPageable();
            if (pageable.isPaged()) {
                Pagination pagination = new Pagination();
                pagination.setPageNumber(pageable.getPageNumber());
                pagination.setPageSize(pageable.getPageSize());
                pagination.setTotalElements(page.getTotalElements());
                this.response.setPageable(pagination);
                this.response.setData(page.getContent());
            }
        } else if (data instanceof Slice) {
            this.response.setCollection(true);
            Slice slice = (Slice) data;
            Pageable pageable = slice.getPageable();
            if (pageable.isPaged()) {
                Pagination pagination = new Pagination();
                pagination.setPageNumber(pageable.getPageNumber());
                pagination.setPageSize(pageable.getPageSize());
                pagination.setTotalElements(-1);
                this.response.setPageable(pagination);
                this.response.setData(slice.getContent());
            }
        }
        return this;
    }

    public <T> T to(Class<T> type) {
        return JsonUtil.fromJson(this.response.getData(), type);
    }

    public <T> T to(Type type) {
        return JsonUtil.fromJson(this.response.getData(), type);
    }

    @Override
    protected RestResponse response(boolean enableDebugInfo) {
        return this.response;
    }
}
