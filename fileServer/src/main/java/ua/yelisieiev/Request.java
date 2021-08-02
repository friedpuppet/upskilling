package ua.yelisieiev;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String uri;
    private final Map<String, String> Headers = new HashMap<>();
    private HttpMethod method;

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setHeader(String name, String value) {
        Headers.put(name, value);
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return uri.equals(request.uri) && Headers.equals(request.Headers) && method == request.method;
    }

}
