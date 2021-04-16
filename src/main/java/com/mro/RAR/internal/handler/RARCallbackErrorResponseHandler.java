package com.mro.RAR.internal.handler;

import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.common.utils.ExceptionFactory;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.RARException;
import com.mro.RAR.exception.ResponseParseException;
import com.mro.RAR.internal.parser.JAXBResponseParser;
import com.mro.RAR.model.RARErrorResult;
import org.apache.http.HttpStatus;

import static com.mro.RAR.RARUtils.safeCloseResponse;

public class RARCallbackErrorResponseHandler implements ResponseHandler {

    @Override
    public void handle(ResponseMessage response) throws RARException, ClientException {
        if (response.getStatusCode() == HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION) {
            JAXBResponseParser parser = new JAXBResponseParser(RARErrorResult.class);
            try {
                RARErrorResult errorResult = (RARErrorResult) parser.parse(response);
                throw ExceptionFactory.createRARException(errorResult, response.getErrorResponseAsString());
            } catch (ResponseParseException e) {
                throw ExceptionFactory.createInvalidResponseException(null,
                        response.getErrorResponseAsString(), e);
            } finally {
                safeCloseResponse(response);
            }
        }
    }
}
