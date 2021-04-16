package com.mro.RAR.internal;


import com.mro.RAR.ServiceClient;
import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.utils.IOUtils;
import com.mro.RAR.common.utils.JacksonUtils;
import com.mro.RAR.common.utils.LogUtils;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.ServiceException;
import com.mro.RAR.internal.parser.ResponseParsers;
import com.mro.RAR.model.PutObjectRequest;
import com.mro.RAR.model.QueryObjectRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RARObjectOperation extends RAROperation {

    public RARObjectOperation(ServiceClient client, CredentialsProvider credsProvider) {
        super(client, credsProvider);
    }


    public String inquirySaveAndStart(PutObjectRequest request) {

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> params = request.getParameters();

        String s = JacksonUtils.writeToString(request.getMetadata());
        InputStream inputStream = IOUtils.stringAsInput(s);

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/inquiries/saveAndStart")
//                .setEndpoint(this.getEndpoint().toString() + "/v1/saas/post")
                .setMethod(HttpMethod.POST)
                .setParameters(params)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();

        return this.doOperation(requestMessage, responseParsers.saveInquiryResponseParser, true, null, null);
    }

    public String updataInquiry(PutObjectRequest request) {
        Map headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.putAll(request.getUriParams());

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/updateInquiry")
//                .setEndpoint(this.getEndpoint().toString() + "/v1/saas/post")
                .setMethod(HttpMethod.POST)
                .setParameters(parameters)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return this.doOperation(requestMessage, responseParsers.updateInquiryResponseParser, true, null, null);
    }

    public <T> List<T> makeOrderByBuyer(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> params = request.getParameters();
        params.put("cloud", "true");
        params.put("confirm", "true");
        params.put("priceToPlat", "false");

        Map<String, String> headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/create/make-order")
                .setMethod(HttpMethod.POST)
                .setParameters(params)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();

        try {
            return (List<T>) this.doOperation(requestMessage, new ResponseParsers.CreateOrderResponseParser<T>(clazz), true, null, null);
        }catch (ClientException var1) {
            LogUtils.logException(var1.getErrorMessage(), var1);
        } catch (ServiceException var2) {
            LogUtils.logException(var2.getErrorMessage(), var2);
        }
        return null;
    }

    public <T> T uploadGenContractDoc(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("templateId", "0");
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/templates/generator")
                .setMethod(HttpMethod.GET)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setParameters(parameters)
                .build();

        return (T) this.doOperation(requestMessage, new ResponseParsers.UploadDocResponseParser<T>(clazz), true, null, null);
    }

    public <T> T uploadSourceDoc(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        String orderId = parameters.get("orderId");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/" + orderId + "/contract/doc")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setParameters(parameters)
                .setInputStream(inputStream)
                .build();

        return (T) this.doOperation(requestMessage, new ResponseParsers.UploadDocResponseParser<T>(clazz), true, null, null);
    }

    public <T> T uploadInquiryDoc(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/contract/doc/inquiry")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();

        return (T) this.doOperation(requestMessage, new ResponseParsers.UploadInquiryDocResponseParser<T>(clazz), true, null, null);
    }

    public <T> T uploadOfferDoc(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/contract/doc/offer")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();

        return (T) this.doOperation(requestMessage, new ResponseParsers.UploadInquiryDocResponseParser<T>(clazz), true, null, null);
    }

    public String updateBillItem(PutObjectRequest request) {

        Map<String, String> parameters = request.getParameters();

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/updateBillItem")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setInputStream(inputStream)
                .setHeaders(headers)
                .build();

        return this.doOperation(requestMessage, new ResponseParsers.UpdateBillItemResponseParser(), true, null, null);
    }

    public <T> T insertPaymentItem(PutObjectRequest request, Class<T> clazz) {

        Map<String, String> params = request.getParameters();
        params.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/payments/")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setHeaders(headers)
                .setParameters(params)
                .setInputStream(inputStream)
                .build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.InsertPaymentItem<T>(clazz), true, null, null);
    }

    public String updateStockTableId(PutObjectRequest request) {
        Map<String, String> parameters = request.getParameters();

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/putStockTableCloud")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return this.doOperation(requestMessage, responseParsers.updateStockTableIdResponseParser, true, null, null);
    }

    public <T> List<T> stocking(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/stock/stocking")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return (List<T>) this.doOperation(requestMessage, new ResponseParsers.StockingResponseParser<T>(clazz), true, null, null);
    }

    public <T> T updateCkmroBill(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/bill/updateBill")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.UpdateCkmroBillResponseParser<T>(clazz), true, null, null);
    }

    public <T> T updatePlatCompany(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/basic/companies/updateCompany")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.UpdatePlatCompanyResponseParser<T>(clazz), true, null, null);
    }

    public String updateSuggestItem(PutObjectRequest request) {
        Map<String, String> parameters = request.getParameters();

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));
        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/updateSuggestItem")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();

        return this.doOperation(requestMessage, responseParsers.updateSuggestItemResponseParse, true, null, null);
    }

    public <T> String requestFastTransaction(PutObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");
        parameters.put("accountId", "unknow");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        InputStream inputStream = IOUtils.stringAsInput(JacksonUtils.writeToString(request.getMetadata()));
        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/fastTransaction/requestFastTransaction")
                .setMethod(HttpMethod.POST)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        return this.doOperation(requestMessage, responseParsers.requestFastTransaction, true, null, null);
    }

}
