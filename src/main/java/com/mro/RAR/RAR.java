package com.mro.RAR;


import com.mro.RAR.auth.Credentials;
import com.mro.RAR.event.ExceptionHandler;
import com.mro.RAR.event.LoopHandler;
import com.mro.RAR.event.RecordHandler;
import com.mro.RAR.model.options.HttpMethod;

import java.util.List;
import java.util.Map;

public interface RAR {
    /* 需要提供接口的方法 */

    /**
     * 验证Credentials
     *
     * @param credentials
     */
    boolean detectionCredential(Credentials credentials, boolean update);

    /**
     * 配置 异常 handler
     */
    void configExceptionHandler(ExceptionHandler exceptionHandler);

    /**
     * 配置 记录 handler
     */
    void configRecordHandler(RecordHandler recordHandler);

    /**
     * 添加轮询 inquiryId
     */
    void addIdLoop(String inquiryId, int type);

    /**
     * 删除轮询 id
     */
    void sumbitLoop(String id, int type);

    /**
     * 开启轮询的唯一入口
     *
     * @param loopHandler 轮询数据处理策略
     */
    void loop(LoopHandler loopHandler);

    void isUseLoop(Boolean useLoop);

    boolean isLoopKeepAlive();

    void setLoopHandler(LoopHandler loopHandler);

    /**
     * 关闭OSS实例(资源+轮询),在它的shutdown()被调用后无法使用。block
     */
    void shutdown();

    /**
     * 获取credential 中 user info
     */
    <T> T getUserInfo(String companyId, Class<T> clazz);

    <T> T getPaymentPlatform(String comapnyId, Class<T> clazz);

    <T> String inquirySaveAndStart(T payLoad, boolean saveSetting, String companyId);

    <T> String updateInquiry(T payLoad, String comapnyId);

    Map getInquiryChange(String inquiryId);

    Map getInquiry(String inquiryId, String companyId);

    <T> List<T> makeOrderByBuyer(List<T> payLoad, Class<T> clazz, String comapnyId);

    Map<String, Integer> getOrderChange(String orderId);

    Map getOrder(String orderId, String companyId);

    Map<String, Integer> getOrderStepChange(String orderId);

    Map getOrderStep(String orderId, String companyId);

    <T> T confirmOfferContract(String orderId, String docId, Class<T> clazz, String comapnyId);

    <T> T rejectOfferContract(String orderId, String docId, Class<T> clazz, String comapnyId);

    <T> T uploadGenContractDoc(String orderId, String cloudDoc, Class<T> clazz, String comapnyId);

    <T> T uploadSourceDoc(String orderId, Object doc, Class<T> clazz, String comapnyId);

    <T> T uploadInquiryDoc(String orderId, String docId, String fileId, Class<T> clazz, String comapnyId);

    <T> T uploadOfferDoc(String orderId, String docId, Map map, Class<T> clazz, String comapnyId);

    <T> T insertPaymentItem(T payLoad, Class<T> clazz, String comapnyId);

    String updateStockTableId(Map<String, String> map, String comapnyId);

    <T> List<T> stocking(List<T> tables, Class<T> clazz, String comapnyId);

    String updateBillItem(Map map, String comapnyId);

    <T> T receiptBillItem(String billItemId, Class<T> clazz, String comapnyId);

    <T> T cancelBillItem(String billItemId, Class<T> clazz, String comapnyId);

    <T> T urgedOffer(String inquiryId, Class<T> clazz, String companyId);

    <T> T updateExpireInquiry(String inquiryId, Long expire, Class<T> clazz, String comapnyId);

    String cancelOrder(String id, String comapnyId);

    <T> T updateCkmroBill(T data, Class<T> clazz, String comapnyId);

    <T> T updatePlatCompany(T data, Class<T> clazz, String comapnyId);

    List<String> getOrderCloseItem(String orderId, String comapnyId);

    Map getAdminInform(String comapnyId);

    String updateSuggestItem(String inquiryId, String itemId, String brandName, String comapnyId);

    Map<String,String> getFastTransaction(String comapnyId);

    <T> String requestFastTransaction(T data, Class<T> clazz, String comapnyId);

    String sureFastTransaction(boolean confirm, String comapnyId);

    String deleteFastTransaction(String comapnyId);

    Integer getOrderStatus(String itemId, String orderId, String companyId);

    /**
     * 使用oss作为中间件上传
     */
    String uploadFile(String endpoint, String path, String bucket);
}
