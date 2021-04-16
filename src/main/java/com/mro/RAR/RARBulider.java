package com.mro.RAR;

import com.mro.RAR.auth.CredentialsHandler;

public interface RARBulider {

    RAR build(CredentialsHandler credentialsHandler, boolean var3);

    RAR build(String var1, CredentialsHandler credentialsHandler, boolean var4);
}
