package com.mro.RAR.event;

import java.util.Set;

public interface LoopHandler {

    /**
     * 轮询策略,在方法内处理异常
     */
    void LoopStrategy() throws Exception;

    /**
     * 不要抛出异常,在这里统一处理,防止loop线程死亡
     */
    boolean handler(String inquiryId, int type) throws Exception;

    /**
     * 获取
     */
    Set<String> getList(int type);

    /**
     * 提交已处理Id
     */
    void sumbit(String s, int type);

    void add(String inquiryId, int type);

}
