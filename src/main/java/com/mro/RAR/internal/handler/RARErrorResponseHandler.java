package com.mro.RAR.internal.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.RARException;

public class RARErrorResponseHandler implements ResponseHandler {
//    Logger logger = LoggerFactory.getLogger(RARErrorResponseHandler.class);

    private ObjectMapper objectMapper;

    public RARErrorResponseHandler() {
        objectMapper = new ObjectMapper();
    }

    public void handle(ResponseMessage response) throws RARException, ClientException {
        String requestId = response.getrequestUri();
        if (!response.isSuccessful()) {
//            int statusCode = response.getStatusCode();
//            if (response.getContent() == null) {
//                if (statusCode == 404) {
//                    throw ExceptionFactory.createRARException(requestId, "NoSuchKey", "Not Found");
//                } else if (statusCode == 304) {
//                    throw ExceptionFactory.createRARException(requestId, "NotModified", "Not Modified");
//                } else if (statusCode == 412) {
//                    throw ExceptionFactory.createRARException(requestId, "PreconditionFailed", "Precondition Failed");
//                } else if (statusCode == 401) {
//
//                } else {
//                    throw ExceptionFactory.createUnknownRARException(requestId, statusCode);
//                }
//            } else {
//                try {
//                    HashMap<String, Object> map = (HashMap<String, Object>) objectMapper.readValue(response.getContent(), Map.class);
////                    logger.info("error:" + map.get("error"));
//                    LogUtils.logInfo("error:" + map.get("error"));
//                    if ("invalid_token".equals(map.get("error"))) {
////                        logger.info("invalid , access token again");
//                        LogUtils.logInfo("invalid , access token again");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    RARUtils.safeCloseResponse(response);
//                }
//            }
        }
    }
}

