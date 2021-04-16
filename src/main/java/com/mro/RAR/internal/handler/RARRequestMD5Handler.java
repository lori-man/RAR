package com.mro.RAR.internal.handler;


import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.utils.MD5Util;

import java.net.URI;
import java.util.Date;
import java.util.Map;

public class RARRequestMD5Handler implements RequestHandler {
    private CredentialsProvider credentialsProvider;
    private URI endPoint;

    public RARRequestMD5Handler(CredentialsProvider credentialsProvider, URI endPoint) {
        this.endPoint = endPoint;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public void handle(RequestMessage message) {
        String requestPath = message.getEndpoint().toString();
        String path = requestPath.replace(endPoint.toString(), "");

        String date = Long.valueOf(new Date().getTime()).toString();
        Map<String, String> headers = message.getHeaders();
        String token = headers.get("authorization");
        if (token == null) {
            return;
        }
        String result = MD5Util.md5(path, token.replace("Bearer ", ""), date);

        message.getParameters().put("urlKey", result);
        message.getParameters().put("urlTime", date);

    }


}
