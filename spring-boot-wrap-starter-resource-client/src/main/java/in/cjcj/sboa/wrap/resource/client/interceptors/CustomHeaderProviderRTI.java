package in.cjcj.sboa.wrap.resource.client.interceptors;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;

public class CustomHeaderProviderRTI implements ClientHttpRequestInterceptor {
    private final String HEADER;
    private final String TOKEN;

    public CustomHeaderProviderRTI(String HEADER, String TOKEN) {
        Assert.notNull(HEADER, "HEADER Value should not be null");
        Assert.notNull(TOKEN, "TOKEN Value should not be null");
        this.HEADER = HEADER;
        this.TOKEN = TOKEN;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(HEADER, TOKEN);
        return execution.execute(request, body);
    }
}
