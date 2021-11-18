package in.cjcj.sboa.wrap.rest.filters;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class NoOpServletRequestLoggingFilter extends CommonsRequestLoggingFilter {
    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return false;
    }
}
