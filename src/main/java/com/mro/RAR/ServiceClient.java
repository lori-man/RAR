package com.mro.RAR;

import com.mro.RAR.auth.Credentials;
import com.mro.RAR.auth.RequestSigner;
import com.mro.RAR.common.*;
import com.mro.RAR.common.utils.*;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.RARException;
import com.mro.RAR.exception.ServiceException;
import com.mro.RAR.internal.handler.RequestHandler;
import com.mro.RAR.internal.handler.ResponseHandler;
import com.mro.RAR.model.options.HttpMethod;
import com.mro.RAR.model.options.Protocol;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * httpClient service 执行客户端
 */
public class ServiceClient {

    protected ClientConfiguration config;

    protected static HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
    private static Method setNormalizeUriMethod = null;
    protected CloseableHttpClient httpClient;
    protected HttpClientConnectionManager connectionManager ;
    protected RequestConfig requestConfig;
    protected CredentialsProvider credentialsProvider;
    protected HttpHost proxyHttpHost;
    protected AuthCache authCache;

    public ServiceClient(ClientConfiguration config) {
        this.config = config;
        this.connectionManager=createHttpClientConnectionManager();
        this.httpClient = this.createHttpClient(this.connectionManager);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(config.getConnectionTimeout());
        requestConfigBuilder.setSocketTimeout(config.getSocketTimeout());
        requestConfigBuilder.setConnectionRequestTimeout(config.getConnectionRequestTimeout());
        String proxyHost = config.getProxyHost();
        int proxyPort = config.getProxyPort();
        if (proxyHost != null && proxyPort > 0) {
            this.proxyHttpHost = new HttpHost(proxyHost, proxyPort);
            requestConfigBuilder.setProxy(this.proxyHttpHost);
            String proxyUsername = config.getProxyUsername();
            String proxyPassword = config.getProxyPassword();
            String proxyDomain = config.getProxyDomain();
            String proxyWorkstation = config.getProxyWorkstation();
            if (proxyUsername != null && proxyPassword != null) {
                this.credentialsProvider = new BasicCredentialsProvider();
                this.credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), new NTCredentials(proxyUsername, proxyPassword, proxyWorkstation, proxyDomain));
                this.authCache = new BasicAuthCache();
                this.authCache.put(this.proxyHttpHost, new BasicScheme());
            }
        }
        if (setNormalizeUriMethod != null) {
            try {
                setNormalizeUriMethod.invoke(requestConfigBuilder, false);
            } catch (Exception var9) {
                ;
            }
        }

        this.requestConfig = requestConfigBuilder.build();
    }

    public ResponseMessage sendRequest(RequestMessage request, ExecutionContext context) throws ServiceException, ClientException {
        CodingUtils.assertParameterNotNull(request, "request");
        CodingUtils.assertParameterNotNull(context, "context");

        ResponseMessage var3;

        var3 = this.sendRequestImpl(request, context);

        return var3;
    }

    public ResponseMessage sendRequestImpl(RequestMessage request, ExecutionContext context) throws ClientException, ServiceException {
        RetryStrategy retryStrategy = this.getDefaultRetryStrategy();

        Iterator i$ = this.config.getSignerHandlers().iterator();

        while (i$.hasNext()) {
            RequestSigner signer = (RequestSigner) i$.next();
            signer.sign(request);
        }

        int retries = 0;
        ResponseMessage response = null;

        while (true) {
            while (true) {
                if (retries > 3) {
                    throw new ServiceException("requestId:" + request.getEndpoint() + "Cancel the request and throw an exception after three retries");
                }
                try {
                    if (retries > 0) {
                        this.pause(retries, retryStrategy);
                    }

                    //HTTP Request Signer
                    RequestSigner signer = context.getSigner();
                    if (signer != null) {
                        signer.sign(request);
                    }

                    //HTTP Request preprocessing
                    this.handleRequest(request, context.getResquestHandlers());

                    //Builds the HTTP request and context using the specified request parameters.
                    Request httpRequest = this.buildRequest(request);

                    //send http
                    long startTime = System.currentTimeMillis();
                    response = this.sendRequestCore(httpRequest,context);
                    long duration = System.currentTimeMillis() - startTime;
                    if (duration > this.config.getSlowRequestsThreshold()) {
                        LogUtils.logWarn("duration is longer than slowRequestsThreshold");
                    }

                    //HTTP response preprocessing
                    this.handleResponse(response, context.getResponseHandlers());

                    ResponseMessage var12 = response;
                    return var12;
                } catch (ServiceException var20) {
                    LogUtils.logInfo("[Server]Unable to execute HTTP request: " + request.toString());
                    var20.setRequestId(request.getEndpoint().toString());
                    this.closeResponseSilently(response);
                } catch (ClientException var21) {
                    var21.setRequestId(request.getEndpoint().toString());
                    LogUtils.logInfo("[Client]Unable to execute HTTP request: "+request.toString());
                    this.closeResponseSilently(response);
                } catch (Exception var22) {
                    LogUtils.logInfo("[Unknown]Unable to execute HTTP request: " + request.toString());
                    this.closeResponseSilently(response);
                } finally {
                    ++retries;
                }
            }
        }
    }

    /**
     * reuqestMessage转request
     */
    private Request buildRequest(RequestMessage requestMessage) throws ClientException {
        Request request = new Request();
        request.setMethod(requestMessage.getHttpMethod());
        request.setUseChunkEncoding(requestMessage.isUseChunkEncoding());
        if (requestMessage.isUseUrlSignature()) {
            request.setUrl(requestMessage.getAbsoluteUrl().toString());
            request.setUseUrlSignature(true);
            request.setContent(requestMessage.getContent());
            request.setContentLength(requestMessage.getContentLength());
            request.setHeaders(requestMessage.getHeaders());
            return request;
        } else {
            request.setHeaders(requestMessage.getHeaders());
            if (request.getHeaders() != null) {
                HttpUtil.convertHeaderCharsetToIso88591(request.getHeaders());
            }

            String delimiter = "/";
            String uri = requestMessage.getEndpoint().toString();

            String paramString = HttpUtil.paramToQueryString(requestMessage.getParameters(), "UTF-8");
            boolean requestHasNoPayload = requestMessage.getContent() != null;
            boolean requestIsPost = requestMessage.getHttpMethod() == HttpMethod.POST;
            boolean putParamsInUri = !requestIsPost || requestHasNoPayload;
            if (paramString != null && putParamsInUri) {
                uri = uri + "?" + paramString;
            }

            request.setUrl(uri);
            if (requestIsPost && requestMessage.getContent() == null && paramString != null) {
                try {
                    byte[] buf = paramString.getBytes("UTF-8");
                    ByteArrayInputStream content = new ByteArrayInputStream(buf);
                    request.setContent(content);
                    request.setContentLength((long)buf.length);
                } catch (UnsupportedEncodingException var12) {
                    var12.printStackTrace();
                }
            } else {
                request.setContent(requestMessage.getContent());
                request.setContentLength(requestMessage.getContentLength());
            }

            return request;
        }
    }


    public ResponseMessage sendRequestCore(Request request,ExecutionContext context) throws IOException {
        HttpRequestBase httpRequest = httpRequestFactory.createHttpRequest(request,context);

        HttpClientContext httpContext = this.createHttpContext();
        httpContext.setRequestConfig(this.requestConfig);
        CloseableHttpResponse httpResponse = null;

        try {
            httpResponse = this.httpClient.execute(httpRequest, httpContext);
        } catch (IOException var7) {
            httpRequest.abort();
            LogUtils.logInfo("request请求:" + request.uri + " httpClient 执行失败");
            throw ExceptionFactory.createNetworkException(var7);
        }

        return buildResponse(request, httpResponse);
    }

    /**
     * 将返回的httpResponse加工成ResponseMessage(配置信息from request)
     * @param request
     * @param httpResponse
     * @return
     * @throws IOException
     */
    protected static ResponseMessage buildResponse(Request request, CloseableHttpResponse httpResponse) throws IOException {
        assert httpResponse != null;

        ResponseMessage response = new ResponseMessage(request);
        response.setUrl(request.getUri());
        response.setHttpResponse(httpResponse);
        if (httpResponse.getStatusLine() != null) {
            response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        }

        if (httpResponse.getEntity() != null) {
            if (response.isSuccessful()) {
                response.setContent(httpResponse.getEntity().getContent());
            } else {
                readAndSetErrorResponse(httpResponse.getEntity().getContent(), response);
            }
        }

        Header[] arr$ = httpResponse.getAllHeaders();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Header header = arr$[i$];
            if ("Content-Length".equalsIgnoreCase(header.getName())) {
                response.setContentLength(Long.parseLong(header.getValue()));
            }

            response.addHeader(header.getName(), header.getValue());
        }

        HttpUtil.convertHeaderCharsetFromIso88591(response.getHeaders());
        return response;
    }

    private boolean shouldRetry(Exception exception, RequestMessage request, ResponseMessage response, int retries, RetryStrategy retryStrategy) {
        if (retries >= this.config.getMaxErrorRetry()) {
            return false;
        }  else if (retryStrategy.shouldRetry(exception, request, response, retries)) {
            LogUtils.logInfo("Retrying on " + exception.getClass().getName() + ": " + exception.getMessage());
            return true;
        } else {
            return false;
        }
    }

    private static void readAndSetErrorResponse(InputStream originalContent, ResponseMessage response) throws IOException {
        byte[] contentBytes = IOUtils.readStreamAsByteArray(originalContent);
        response.setErrorResponseAsString(new String(contentBytes));
        response.setContent(new ByteArrayInputStream(contentBytes));
    }

    /**
     * requestHandler来处理request
     */
    private void handleRequest(RequestMessage message, List<RequestHandler> resquestHandlers) throws ServiceException, ClientException {
        Iterator i$ = resquestHandlers.iterator();

        while(i$.hasNext()) {
            RequestHandler h = (RequestHandler)i$.next();
            h.handle(message);
        }

    }

    /**
     * responseHandler来处理response
     */
    private void handleResponse(ResponseMessage response, List<ResponseHandler> responseHandlers) throws ServiceException, ClientException {
        Iterator i$ = responseHandlers.iterator();

        while(i$.hasNext()) {
            ResponseHandler h = (ResponseHandler)i$.next();
            h.handle(response);
        }
    }

    /**
     * 创建httpContext
     * @return
     */
    protected HttpClientContext createHttpContext() {
        HttpClientContext httpContext = HttpClientContext.create();
        return httpContext;
    }

    /**
     * 暂停
     */
    private void pause(int retries, RetryStrategy retryStrategy) throws ClientException {
        long delay = retryStrategy.getPauseDelay(retries);
        LogUtils.logInfo("An retriable error request will be retried after " + delay + "(ms) with attempt times: " + retries);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException var6) {
            throw new ClientException(var6.getMessage(), var6);
        }
    }

    /**
     * 创建httpClient
     */
    protected CloseableHttpClient createHttpClient(HttpClientConnectionManager connectionManager) {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setUserAgent(this.config.getUserAgent())
                .disableContentCompression() //禁用自动内容解压
                .disableAutomaticRetries().build(); //禁用自动请求恢复和重新执行。
    }

    /**
     * 创建connectionManager
     */
    protected HttpClientConnectionManager createHttpClientConnectionManager() {
        SSLContext sslContext = null;

        try {
            sslContext = (new SSLContextBuilder()).loadTrustMaterial((KeyStore)null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Exception var5) {
            throw new ClientException(var5.getMessage());
        }

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        //为受支持的自定义连接套接字工厂创建注册表协议方案。
        Registry<ConnectionSocketFactory> socketFactoryRegistry = registryBuilder
                .register(Protocol.HTTP.toString(), PlainConnectionSocketFactory.getSocketFactory())
                .register(Protocol.HTTPS.toString(), sslSocketFactory).build();
        //创建具有自定义配置的连接管理器。
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        connectionManager.setDefaultMaxPerRoute(this.config.getMaxConnections());
        connectionManager.setMaxTotal(this.config.getMaxConnections());
        connectionManager.setValidateAfterInactivity(this.config.getValidateAfterInactivity());
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(this.config.getSocketTimeout()).setTcpNoDelay(true).build());

        if (this.config.isUseReaper()) {
            IdleConnectionReaper.setIdleConnectionTime(this.config.getIdleConnectionTime());
            IdleConnectionReaper.registerConnectionManager(connectionManager);
        }

        return connectionManager;
    }

    /**
     * 获取默认重试策略
     * @return
     */
    protected RetryStrategy getDefaultRetryStrategy() {
        return new DefaultRetryStrategy();
    }

    /**
     * 关闭response
     */
    private void closeResponseSilently(ResponseMessage response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException var3) {
                ;
            }
        }

    }

    public void shutdown() {
        IdleConnectionReaper.removeConnectionManager(this.connectionManager);
        this.connectionManager.shutdown();
    }

    public static class Request extends HttpMesssage {
        //已加工完后的uri
        private String uri;
        private HttpMethod method;
        private boolean useUrlSignature = false;
        private boolean useChunkEncoding = false;

        public Request() {
        }

        public String getUri() {
            return this.uri;
        }

        public void setUrl(String uri) {
            this.uri = uri;
        }

        public HttpMethod getMethod() {
            return this.method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public boolean isUseUrlSignature() {
            return this.useUrlSignature;
        }

        public void setUseUrlSignature(boolean useUrlSignature) {
            this.useUrlSignature = useUrlSignature;
        }

        public boolean isUseChunkEncoding() {
            return this.useChunkEncoding;
        }

        public void setUseChunkEncoding(boolean useChunkEncoding) {
            this.useChunkEncoding = useChunkEncoding;
        }
    }

    private static class DefaultRetryStrategy extends RetryStrategy {
        private DefaultRetryStrategy() {
        }

        public boolean shouldRetry(Exception ex, RequestMessage request, ResponseMessage response, int retries) {
            String errorCode;
            if (ex instanceof ClientException) {
                errorCode = ((ClientException)ex).getErrorCode();
                if (errorCode.equals("ConnectionTimeout") || errorCode.equals("SocketTimeout") || errorCode.equals("ConnectionRefused") || errorCode.equals("UnknownHost") || errorCode.equals("SocketException")) {
                    return true;
                }

                if (errorCode.equals("NonRepeatableRequest")) {
                    return false;
                }
            }

            if (ex instanceof RARException) {
                errorCode = ((RARException)ex).getErrorCode();
                if (errorCode.equals("InvalidResponse")) {
                    return false;
                }
            }

            if (response != null) {
                int statusCode = response.getStatusCode();
                if (statusCode == 500 || statusCode == 503) {
                    return true;
                }
            }

            return false;
        }
    }



    public ClientConfiguration getConfig() {
        return config;
    }
    public CloseableHttpClient getHttpClient(){
        return this.httpClient;
    }
}
