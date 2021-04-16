package com.mro.RAR.auth;

import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.SignVersion;
import com.mro.RAR.exception.ClientException;

public class RARRequestSigner implements RequestSigner {
    private CredentialsHandler credentialsHandler;
    private SignVersion signatureVersion;

    public RARRequestSigner(CredentialsHandler credentialsHandler, SignVersion signatureVersion) {
        this.credentialsHandler = credentialsHandler;
        this.signatureVersion = signatureVersion;
    }

    /**
     * 为请求添加token
     * @param request
     * @throws ClientException
     */
    public void sign(RequestMessage request) throws ClientException {
//        String userId = this.creds.getAccountId();
//        if (userId.length() > 0 && creds.getTokenStatus()) {
//            if (this.signatureVersion == SignVersion.V2) {
//                request.addHeader("authorization", "Bearer " + creds.getSecurityToken());
//            } else {
//                request.addHeader("authorization", "Bearer " + creds.getSecurityToken());
//            }
//        }
    }
}
