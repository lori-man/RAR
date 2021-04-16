package com.mro.RAR.auth;

import com.mro.RAR.event.RecordHandler;

/**
 * credentials 处理器
 * 处理过程中抛出异常继承于{@link CredentialsException},记录于 {@link RecordHandler} 中
 */
public interface CredentialsHandler {

    void setSecurityToken(Credentials credentials);

    /**
     * 获取credentials
     */
    Credentials getCredentials(String companyId);

    /**
     * 判断是否可使用token
     */
    boolean useSecurityToken(String companyId);


    /**
     * set token status invalid
     */
    void invaildSecurityToken(String companyId);
}
