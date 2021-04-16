package com.mro.RAR;

import com.mro.RAR.auth.Credentials;
import com.mro.RAR.auth.RequestSigner;
import com.mro.RAR.common.RetryStrategy;
import com.mro.RAR.internal.handler.RequestHandler;
import com.mro.RAR.internal.handler.ResponseHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * RAR 操作行为上下文 生命周期:执行一次operation
 */
public class ExecutionContext {
    private RequestSigner signer;
    private List<RequestHandler> requestHandlers = new LinkedList();
    private List<ResponseHandler> responseHandlers = new LinkedList();
    private List<RequestSigner> signerHandlers = new LinkedList();
    private String charset = "utf-8";
    private RetryStrategy retryStrategy;
    private Credentials credentials;

    public ExecutionContext() {
    }

    public RetryStrategy getRetryStrategy() {
        return this.retryStrategy;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String defaultEncoding) {
        this.charset = defaultEncoding;
    }

    public RequestSigner getSigner() {
        return this.signer;
    }

    public void setSigner(RequestSigner signer) {
        this.signer = signer;
    }

    public List<ResponseHandler> getResponseHandlers() {
        return this.responseHandlers;
    }

    public void addResponseHandler(ResponseHandler handler) {
        this.responseHandlers.add(handler);
    }

    public void insertResponseHandler(int position, ResponseHandler handler) {
        this.responseHandlers.add(position, handler);
    }

    public void removeResponseHandler(ResponseHandler handler) {
        this.responseHandlers.remove(handler);
    }

    public List<RequestHandler> getResquestHandlers() {
        return this.requestHandlers;
    }

    public void addRequestHandler(RequestHandler handler) {
        this.requestHandlers.add(handler);
    }

    public void insertRequestHandler(int position, RequestHandler handler) {
        this.requestHandlers.add(position, handler);
    }

    public void removeRequestHandler(RequestHandler handler) {
        this.requestHandlers.remove(handler);
    }

    public List<RequestSigner> getSignerHandlers() {
        return this.signerHandlers;
    }

    public void addSignerHandler(RequestSigner handler) {
        this.signerHandlers.add(handler);
    }

    public void insertSignerHandler(int position, RequestSigner handler) {
        this.signerHandlers.add(position, handler);
    }

    public void removeSignerHandler(RequestSigner handler) {
        this.signerHandlers.remove(handler);
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
