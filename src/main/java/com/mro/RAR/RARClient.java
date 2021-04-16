package com.mro.RAR;


import com.mro.RAR.auth.Credentials;
import com.mro.RAR.auth.CredentialsHandler;
import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.auth.DefaultCredentialProvider;
import com.mro.RAR.common.utils.LogUtils;
import com.mro.RAR.event.ExceptionHandler;
import com.mro.RAR.event.LoopHandler;
import com.mro.RAR.event.RARLoop;
import com.mro.RAR.event.RecordHandler;
import com.mro.RAR.internal.RARObjectOperation;
import com.mro.RAR.internal.RARQueryOpeartion;
import com.mro.RAR.model.PutObjectRequest;
import com.mro.RAR.model.QueryObjectRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mro.RAR.common.utils.CodingUtils.assertParameterNotNull;

public class RARClient implements RAR {

    private CredentialsProvider credsProvider;
    private URI endpoint;
    private ServiceClient serviceClient;

    private RARObjectOperation rarObjectOperation;
    private RARQueryOpeartion rarQueryOpeartion;
    private RARLoop RARLoop;
    private LoopHandler loopHandler;
    private Boolean useLoop; //是否轮询
    private final Boolean useRAR;  //是否使用RAR功能

    @Deprecated
    public RARClient(CredentialsHandler credentialsHandler,boolean useRAR) {
        this("https://dev.gateway.ckmro.com", credentialsHandler, useRAR);
    }

    @Deprecated
    public RARClient(String endpoint, CredentialsHandler credentialsHandler, boolean useRAR) {
        this(endpoint,credentialsHandler, (ClientConfiguration) null, useRAR);
    }


    public RARClient(String endpoint, CredentialsHandler credentialsHandler,
                     ClientConfiguration config, boolean useRAR) {
        this.useRAR = useRAR;
        this.useLoop = false;
        this.credsProvider = new DefaultCredentialProvider(credentialsHandler);
        config = config == null ? new ClientConfiguration() : config;
        this.serviceClient = new ServiceClient(config);

        this.initOperations();
        this.setEndpoint(endpoint);
    }

    /**
     * 初始化operation
     */
    private void initOperations() {
        this.rarObjectOperation = new RARObjectOperation(this.serviceClient, this.credsProvider);
        this.rarQueryOpeartion = new RARQueryOpeartion(this.serviceClient, this.credsProvider);

    }

    public void setEndpoint(String endpoint) {
        try {
            this.endpoint = new URI(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //为所有的opetation添加endpiont
        this.rarObjectOperation.setEndpoint(this.endpoint);
        this.rarQueryOpeartion.setEndpoint(this.endpoint);

    }

    public boolean detectionCredential(Credentials credentials, boolean update) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return false;
        }
        return rarQueryOpeartion.detectionCredential(credentials, update);
    }


    public void configExceptionHandler(ExceptionHandler exceptionHandler) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }
        LogUtils.setExceptionHandler(exceptionHandler);
    }

    public void configRecordHandler(RecordHandler recordHandler) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }
        this.rarObjectOperation.setRecordHandler(recordHandler);
        this.rarQueryOpeartion.setRecordHandler(recordHandler);
    }

    public void loop(LoopHandler loopHandler) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        if (!useLoop) {
            return;
        }
        if (loopHandler == null) {
            return;
        }

        synchronized (this.useLoop) {
            if (RARLoop == null) {
                this.RARLoop = new RARLoop(loopHandler);
            } else {
                if (RARLoop.isLoop()) {
                    LogUtils.logWarn("RARLoop hasen running");
                } else {
                    this.RARLoop.openLoop(loopHandler);
                }
            }
            this.loopHandler = loopHandler;
        }
    }

    public void addIdLoop(String inquiryId,int type) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        if (loopHandler != null) {
            loopHandler.add(inquiryId,type);
        }
    }

    public void sumbitLoop(String id, int type) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        if (loopHandler != null) {
            loopHandler.sumbit(id,type);
        }
    }

    public void isUseLoop(Boolean useLoop) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        this.useLoop = useLoop;
    }

    public boolean isLoopKeepAlive() {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return false;
        }

        if (RARLoop != null) {
            return RARLoop.isLoop();
        }
        return false;
    }


    public void setLoopHandler(LoopHandler loopHandler) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        this.loopHandler = loopHandler;
    }

    public void shutdown() {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return;
        }

        boolean loopKeepAlive = isLoopKeepAlive();
        if (useLoop && loopKeepAlive) {
            RARLoop.closeLoop();
        }
        try {
            serviceClient.shutdown();
        } catch (Exception e) {
            LogUtils.logException(e.getMessage(), e);
        }
    }

    public <T> T getUserInfo(String companyId, Class<T> clazz) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }
        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "getUserInfo");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.getUserInfo(request, clazz);
    }

    public <T> T getPaymentPlatform(String comapnyId, Class<T> clazz) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "getPaymentPlatform");
        parameters.put("RAR:companyId", comapnyId);
        return this.rarQueryOpeartion.getPaymentPlatform(request, clazz);
    }

    public <T> String inquirySaveAndStart(T payLoad, boolean saveSetting, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(payLoad);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "inquirySaveAndStart");
        parameters.put("RAR:companyId", companyId);
        parameters.put("saveSetting", String.valueOf(saveSetting));
        return inquirySaveAndStart(request);
    }


    public String inquirySaveAndStart(PutObjectRequest request) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        return this.rarObjectOperation.inquirySaveAndStart(request);
    }

    public Map getInquiry(String inquiryId, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("inquiryId", inquiryId);
        parameters.put("RAR:companyId", companyId);
        parameters.put("RAR:method", "getInquiry");
        return this.rarQueryOpeartion.getInquiry(request);
    }

    public <T> String updateInquiry(T payLoad, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(payLoad);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "updateInquiry");
        parameters.put("RAR:companyId", companyId);
        return updateInquiry(request);
    }

    public String updateInquiry(PutObjectRequest request) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        return this.rarObjectOperation.updataInquiry(request);
    }


    public <T> List<T> makeOrderByBuyer(List<T> payLoad, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(payLoad);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "makeOrderByBuyer");
        parameters.put("RAR:companyId", companyId);
        return makeOrderByBuyer(request, clazz);
    }

    public <T> List<T> makeOrderByBuyer(PutObjectRequest request, Class<T> clazz) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        return rarObjectOperation.makeOrderByBuyer(request, clazz);
    }

    public Map getInquiryChange(String inquiryId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> params = new HashMap<>();
        params.put("inquiryId", inquiryId);
        request.setParameters(params);
        return rarQueryOpeartion.getInquiryChange(request);
    }

    public Map<String,Integer> getOrderChange(String orderId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        request.setParameters(params);
        return rarQueryOpeartion.getOrderChange(request);
    }

    public Map getOrder(String orderId, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("RAR:method", "getOrder");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.getOrder(request);
    }

    public Map<String,Integer> getOrderStepChange(String orderId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        QueryObjectRequest request = new QueryObjectRequest();
        request.getParameters().put("orderId", orderId);
        return rarQueryOpeartion.getOrderStepChange(request);
    }

    public Map getOrderStep(String orderId, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("RAR:method", "getOrderStep");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.getOrderStep(request);
    }

    public <T> T confirmOfferContract(String orderId, String docId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("docId", docId);
        parameters.put("RAR:method", "confirmOfferContract");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.confirmOfferContract(request, clazz);
    }

    public <T> T rejectOfferContract(String orderId, String docId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("id", orderId);
        parameters.put("docId", docId);
        parameters.put("RAR:method", "rejectOfferContract");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.rejectOfferContract(request, clazz);
    }

    public <T> T uploadGenContractDoc(String orderId, String cloudDoc, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("cloudDoc", cloudDoc);
        parameters.put("RAR:method", "uploadGenContractDoc");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.uploadGenContractDoc(request, clazz);
    }

    public <T> T uploadSourceDoc(String orderId, Object doc, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(doc);
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("RAR:method", "uploadSourceDoc");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.uploadSourceDoc(request, clazz);
    }

    public <T> T uploadInquiryDoc(String orderId, String docId, String fileId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(new HashMap<String, String>());
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("docId", docId);
        parameters.put("fileId", fileId);
        parameters.put("RAR:method", "uploadInquiryDoc");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.uploadInquiryDoc(request, clazz);
    }

    public <T> T uploadOfferDoc(String orderId, String docId, Map map, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(map);
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("docId", docId);
        parameters.put("fileId", "");
        parameters.put("RAR:method", "uploadOfferDoc");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.uploadOfferDoc(request, clazz);
    }

    public String updateBillItem(Map map, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(map);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "updateBillItem");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.updateBillItem(request);
    }

    public <T> T insertPaymentItem(T payLoad, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(payLoad);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "insertPaymentItem");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.insertPaymentItem(request, clazz);
    }

    public String updateStockTableId(Map<String, String> map, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(map);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "updateStockTableId");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.updateStockTableId(request);
    }

    public <T> List<T> stocking(List<T> tables, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(tables);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "stocking");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.stocking(request, clazz);
    }

    public <T> T receiptBillItem(String billItemId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("billItemId", billItemId);
        parameters.put("RAR:method", "receiptBillItem");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.receiptBillItem(request, clazz);
    }

    public <T> T cancelBillItem(String billItemId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("billItemId", billItemId);
        parameters.put("RAR:method", "cancelBillItem");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.cancelBillItem(request, clazz);
    }

    public <T> T urgedOffer(String inquiryId, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("inquiryId", inquiryId);
        parameters.put("companyId", companyId);
        parameters.put("RAR:method", "urgedOffer");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.urgedOffer(request, clazz);
    }

    public <T> T updateExpireInquiry(String inquiryId, Long expire, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("inquiryId", inquiryId);
        parameters.put("expire", String.valueOf(expire));
        parameters.put("RAR:method", "updateExpireInquiry");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.updateExpireInquiry(request, clazz);
    }

    public String cancelOrder(String id, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("id", id);
        parameters.put("RAR:method", "cancelOrder");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.cancelOrder(request);
    }

    public <T> T updateCkmroBill(T data, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(data);
        Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:method", "updateCkmroBill");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.updateCkmroBill(request, clazz);
    }

    public <T> T updatePlatCompany(T data, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(data);
        return rarObjectOperation.updatePlatCompany(request, clazz);
    }

    public List<String> getOrderCloseItem(String orderId, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("orderId", orderId);
        parameters.put("RAR:method", "getOrderCloseItem");
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.getOrderCloseItem(request);
    }

    public Map getAdminInform(String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        return rarQueryOpeartion.getAdminInform(request);
    }

    public String updateSuggestItem(String inquiryId, String itemId, String brandName, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest("");
        Map<String, String> parameters = request.getParameters();
        parameters.put("inquiryId", inquiryId);
        parameters.put("itemId", itemId);
        parameters.put("brandName", brandName);
        parameters.put("RAR:method", "updateSuggestItem");
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.updateSuggestItem(request);
    }

    public Map<String, String> getFastTransaction(String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        final Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.getFastTransaction(request);
    }

    public <T> String requestFastTransaction(T data, Class<T> clazz, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        PutObjectRequest request = new PutObjectRequest(data);
        final Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:companyId", companyId);
        return rarObjectOperation.requestFastTransaction(request, clazz);
    }

    public String sureFastTransaction(boolean confirm, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("confirm", String.valueOf(confirm));
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.sureFastTransaction(request);
    }

    public String deleteFastTransaction(String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        final Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:companyId", companyId);
        return rarQueryOpeartion.deleteFastTransaction(request);
    }

    public Integer getOrderStatus(String itemId, String orderId, String companyId) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }

        assertParameterNotNull(companyId, "companyId");

        QueryObjectRequest request = new QueryObjectRequest();
        final Map<String, String> parameters = request.getParameters();
        parameters.put("RAR:companyId", companyId);
        parameters.put("orderId", orderId);
        parameters.put("itemId", itemId);
        return rarQueryOpeartion.getOrderStatus(request);
    }

    public String uploadFile(String endpoint, String path, String bucket) {
        if (!useRAR) {
            LogUtils.logInfo("RAR is close , please open it");
            return null;
        }
        QueryObjectRequest request = new QueryObjectRequest();
        Map<String, String> parameters = request.getParameters();
        parameters.put("endpoint", endpoint);
        parameters.put("path", path);
        parameters.put("bucket", bucket);
        return rarQueryOpeartion.uploadFile(request);
    }
}