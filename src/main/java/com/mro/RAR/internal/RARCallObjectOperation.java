package com.mro.RAR.internal;

import com.mro.RAR.ServiceClient;
import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.internal.handler.RARCallbackErrorResponseHandler;
import com.mro.RAR.internal.handler.ResponseHandler;
import com.mro.RAR.internal.parser.ResponseParser;
import com.mro.RAR.model.ObjectMetadata;
import com.mro.RAR.model.CallbackPutObjectRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.util.*;

import static com.mro.RAR.RARUtils.*;

public class RARCallObjectOperation extends RAROperation {

    protected RARCallObjectOperation(ServiceClient client, CredentialsProvider credsProvider) {
        super(client, credsProvider);
    }


    private <RequestType extends CallbackPutObjectRequest, ResponseType> ResponseType executeObjectInternel(
            RequestType originalRequest, ResponseParser<ResponseType> responseParser) {

        ensureCallbackValid(originalRequest.getCallback());
        ObjectMetadata metadata = originalRequest.getMetadata();

        Map<String, String> headers = new HashMap<String, String>();
        populateRequestMetadata(headers, metadata);
        populateRequestCallback(headers, originalRequest.getCallback());

        Map<String, String> params = new LinkedHashMap<String, String>();

        RequestMessage httpRequest = new RARRequestMessageBuilder(getInnerClient()).setEndpoint(getEndpoint())
                .setMethod(HttpMethod.POST).setHeaders(headers)
                .setParameters(params)
                .setInputStream(originalRequest.getInputStream())
                .setOriginalRequest(originalRequest).build();

        List<ResponseHandler> reponseHandlers = new ArrayList<ResponseHandler>();
        reponseHandlers.add(new RARCallbackErrorResponseHandler());

        return null;
    }


    private static boolean isNeedReturnResponse(CallbackPutObjectRequest callbackPutObjectRequest) {
        if (callbackPutObjectRequest.getCallback() != null || callbackPutObjectRequest.getProcess() != null) {
            return true;
        }
        return false;
    }
}
