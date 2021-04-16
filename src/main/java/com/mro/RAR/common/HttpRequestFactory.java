package com.mro.RAR.common;

import com.mro.RAR.ExecutionContext;
import com.mro.RAR.ServiceClient;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.model.options.HttpMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;

import java.util.Iterator;
import java.util.Map;

/**
 * HttpRequest(HttpClient)加工工厂
 */
public class HttpRequestFactory {
    public HttpRequestFactory() {
    }

    /**
     * 将自定义request转换成httpClient需要的httpRequest
     * @param request
     * @return
     */
    public HttpRequestBase createHttpRequest(ServiceClient.Request request, ExecutionContext context) {
        String uri = request.getUri();
        HttpMethod method = request.getMethod();
        Object httpRequest;
        if (method == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(uri);
            if (request.getContent() != null) {
//                postMethod.setEntity(new RepeatableInputStreamEntity(request));
                postMethod.setEntity(buildEntity(request));
            }
            httpRequest = postMethod;
        } else if (method == HttpMethod.PUT) {
            HttpPut putMethod = new HttpPut(uri);
            if (request.getContent() != null) {
                putMethod.setEntity(buildEntity(request));
            }

            httpRequest = putMethod;
        } else if (method == HttpMethod.GET) {
            httpRequest = new HttpGet(uri);
        } else if (method == HttpMethod.DELETE) {
            httpRequest = new HttpDelete(uri);
        } else if (method == HttpMethod.HEAD) {
            httpRequest = new HttpHead(uri);
        } else {
            if (method != HttpMethod.OPTIONS) {
                throw new ClientException("Unknown HTTP method name: " + method.toString());
            }

            httpRequest = new HttpOptions(uri);
        }

        this.configureRequestHeaders(request, context, (HttpRequestBase)httpRequest);
        return (HttpRequestBase)httpRequest;
    }

    /**
     * 为httpRequest配置请求头(from request)
     * @param request
     * @param httpRequest
     */
    private void configureRequestHeaders(ServiceClient.Request request, ExecutionContext context, HttpRequestBase httpRequest) {
        Iterator i$ = request.getHeaders().entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)i$.next();
            if (!((String)entry.getKey()).equalsIgnoreCase("Content-Length") && !((String)entry.getKey()).equalsIgnoreCase("Host")) {
                httpRequest.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

    }

    private HttpEntity buildEntity(ServiceClient.Request request) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(request.getContent());
        entity.setContentType("application/json");
        return entity;
    }
}
