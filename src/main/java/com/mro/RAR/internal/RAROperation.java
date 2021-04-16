package com.mro.RAR.internal;

import com.mro.RAR.ExecutionContext;
import com.mro.RAR.RARUtils;
import com.mro.RAR.ServiceClient;
import com.mro.RAR.auth.*;
import com.mro.RAR.common.*;
import com.mro.RAR.common.utils.CodingUtils;
import com.mro.RAR.common.utils.LogUtils;
import com.mro.RAR.event.RecordHandler;
import com.mro.RAR.exception.*;
import com.mro.RAR.internal.handler.*;
import com.mro.RAR.internal.parser.ResponseParser;
import com.mro.RAR.internal.parser.ResponseParsers;
import com.mro.RAR.model.WebServiceRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mro.RAR.RARUtils.safeCloseRequest;
import static com.mro.RAR.RARUtils.safeCloseResponse;

public abstract class RAROperation {
    protected volatile URI endpoint;
    protected CredentialsProvider credsProvider;
    protected ServiceClient client;
    protected static RARErrorResponseHandler errorResponseHandler = new RARErrorResponseHandler();
    protected static ResponseParsers responseParsers = new ResponseParsers();
    protected static RetryStrategy noRetryStrategy = new NoRetryStrategy();
    protected RecordHandler recordHandler;

    private int count = 0;

    protected RAROperation(ServiceClient client, CredentialsProvider credsProvider) {
        this.client = client;
        this.credsProvider = credsProvider;
    }

    public URI getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = URI.create(endpoint.toString());
    }

    protected ServiceClient getInnerClient() {
        return this.client;
    }


    protected ResponseMessage send(RequestMessage request, ExecutionContext context, boolean keepResponseOpen)
            throws RARException, ClientException {
        ResponseMessage response = null;
        try {
            response = client.sendRequest(request, context);
            return response;
        } catch (ServiceException e) {
            throw e;
        } finally {
            if (response != null && !keepResponseOpen) {
                safeCloseResponse(response);
            }
        }
    }

    /**
     * 执行操作
     *
     * @param request
     * @param parser
     * @param exceptionHandler 出现网关限制是否重试
     * @param requestHandlers
     * @param reponseHandlers
     * @param <T>
     */
    protected <T> T doOperation(RequestMessage request, ResponseParser<T> parser, boolean exceptionHandler, List<RequestHandler> requestHandlers, List<ResponseHandler> reponseHandlers) {
        Map<String, String> parameters = request.getParameters();
        String companyId = parameters.get("RAR:companyId");

        if (exceptionHandler && companyId == null) {
            throw new InvalidCredentialsException("requeist uri:" + request.getEndpoint() + " companyId exception");
        }

        WebServiceRequest originalRequest = request.getOriginalRequest();
        request.getHeaders().putAll(this.client.getConfig().getDefaultHeaders());
        request.getHeaders().putAll(originalRequest.getHeaders());
        request.getParameters().putAll(originalRequest.getParameters());
        ExecutionContext context = this.createDefaultContext(request.getHttpMethod());

        //处理singer(token),token处理失败由responseParser进行异常处理
        try {
            if (!exceptionHandler || companyId == null || !this.credsProvider.getCredentialsHandler().useSecurityToken(companyId)) {
                throw new InvalidCredentialsException(" judge access is invaild or loop");
            }
            Credentials credentials =
                    this.credsProvider.getCredentialsHandler().getCredentials(companyId);

            if (!credentials.getTokenStatus()) {
                getAuthorizationToken(credentials);
            }

            if (credentials.getTokenStatus()) {
                Map<String, String> headers = request.getHeaders();
                headers.put("authorization", "Bearer " + credentials.getSecurityToken());
            }

        } catch (InvalidCredentialsException var1) {
            if (exceptionHandler) {
                LogUtils.logException("handler request:" + request.getEndpoint() + " " + "[InvalidCredentialsException]:comapnyId" + var1.getCompanyId()
                        + ",userId:" + var1.getUserId()
                        + ",accessKey:" + var1.getAccessKey() + var1.getMessage(), var1);
            }
        }

        /* 根据配置,为上下文添加requestHandler和responseHandler,如:stl,jackson */
        if (requestHandlers != null) {
            Iterator i$ = requestHandlers.iterator();

            while (i$.hasNext()) {
                RequestHandler handler = (RequestHandler) i$.next();
                context.addRequestHandler(handler);
            }
        }

        if (reponseHandlers != null) {
            Iterator i$ = reponseHandlers.iterator();

            while (i$.hasNext()) {
                ResponseHandler handler = (ResponseHandler) i$.next();
                context.addResponseHandler(handler);
            }
        }

        ResponseMessage response = this.send(request, context, true);
        try {
            return parser.parse(response);
        } catch (ResponseParseException var14) {
            if (exceptionHandler && var14 instanceof InvalidTokenException) {
                credsProvider.getCredentialsHandler().invaildSecurityToken(companyId);
            }
            if (exceptionHandler && recordHandler != null) {
                recordHandler.handle(request);
            }
            var14.setUri(request.getEndpoint().toString());
            var14.setParameters(request.getParameters());
            LogUtils.logException("[ResponseParseException] Unable to parse response ", var14, exceptionHandler);
        } finally {
            RARUtils.safeCloseRequest(request);
            safeCloseResponse(response);
        }
        return null;
    }


    protected ExecutionContext createDefaultContext(HttpMethod method) {
        ExecutionContext context = new ExecutionContext();
        //handler禁止对原request数据进行修改,只可添加或者判断处理
        context.setCharset("utf-8");
        context.addRequestHandler(new BasicRequestHeaderHandler(client.getConfig()));
        context.addRequestHandler(new RARRequestMD5Handler(this.credsProvider, this.endpoint));
        context.addResponseHandler(errorResponseHandler);
        if (method == HttpMethod.POST) {
            context.setRetryStrategy(noRetryStrategy);
        }
        return context;
    }


    protected void getAuthorizationToken(Credentials credentials) {
        CodingUtils.assertParameterNotNull(credentials, "credentials");

        String companyId = credentials.getCompanyId();
        String userId = credentials.getAccountId();
        String accessKey = credentials.getAccessKey();

        CodingUtils.assertParameterNotNull(companyId, "companyId");
        CodingUtils.assertParameterNotNull(userId, "userId");
        CodingUtils.assertParameterNotNull(accessKey, "accessKey");


        HashMap<String, String> params = new HashMap<>();
        params.put("ignore", "true");
        params.put("password", accessKey);
        params.put("username", userId);
        params.put("grant_type", "password");
        params.put("scope", "openid");
        params.put("client_secret", "123456");
        params.put("client_id", "mro");
        params.put("captcha", "1234");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("authorization", "Basic bXJvOjEyMzQ1Ng==");

        String authEndPoint = "http://dev.auth.ckmro.com";
        //根据endpoint判断环境 oauth
        if ("https://api.ckmro.com".equals(this.endpoint.toString())) {
            authEndPoint = "https://auth.ckmro.com";
        }

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.client))
                .setEndpoint(authEndPoint + "/oauth/token")
                .setMethod(HttpMethod.POST).setParameters(params)
                .setHeaders(headers)
                .setOriginalRequest(null).build();

        ResponseMessage responseMessage = null;
        try {
            responseMessage = this.client.sendRequest(requestMessage, new ExecutionContext());

            ResponseParsers.AuthTokenResponseParser authTokenResponseParser = responseParsers.authTokenResponseParser;
            Map<String, String> parse = authTokenResponseParser.parse(responseMessage);

            String accessToken = parse.get("access_token");

            if ("invalid_token".equals(accessToken) || "Unauthorized".equals(accessToken)) {
                this.credsProvider.getCredentialsHandler().invaildSecurityToken(companyId);
            }
            if (accessToken != null) {
                credentials.setTokenStatus(true);
                credentials.setSecurityToken(accessToken);
                this.credsProvider.getCredentialsHandler().setSecurityToken(credentials);
            }
        } catch (ClientException var1) {
            LogUtils.logException(var1.getErrorMessage(), var1);
        } catch (ServiceException var2) {
            LogUtils.logException(var2.getErrorMessage(), var2);
        } catch (ResponseParseException var3) {
            TokenException tokenException = new TokenException(var3.getMessage(), requestMessage.getEndpoint().toString());
            tokenException.setCompanyId(companyId);
            tokenException.setUserId(userId);
            tokenException.setAccessKey(accessKey);
            LogUtils.logException("[TokenException] ", tokenException, true);
        } finally {
            safeCloseResponse(responseMessage);
        }
    }

    public void setRecordHandler(RecordHandler recordHandler) {
        this.recordHandler = recordHandler;
    }

    public RecordHandler getRecordHandler() {
        return recordHandler;
    }
}
