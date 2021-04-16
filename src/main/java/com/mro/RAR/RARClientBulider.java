package com.mro.RAR;

import com.mro.RAR.auth.CredentialsHandler;

public class RARClientBulider implements RARBulider {

    public RAR build(CredentialsHandler credentialsHandler,boolean var3) {
        return new RARClient(credentialsHandler, var3);
    }

    public RAR build(String var1, CredentialsHandler credentialsHandler, boolean var4) {
        return new RARClient(var1, credentialsHandler,var4);
    }
}
