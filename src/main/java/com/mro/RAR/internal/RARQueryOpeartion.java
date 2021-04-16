package com.mro.RAR.internal;

import com.mro.RAR.ExecutionContext;
import com.mro.RAR.RARUtils;
import com.mro.RAR.ServiceClient;
import com.mro.RAR.auth.Credentials;
import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.common.utils.CodingUtils;
import com.mro.RAR.common.utils.IOUtils;
import com.mro.RAR.common.utils.LogUtils;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.ResponseParseException;
import com.mro.RAR.exception.ServiceException;
import com.mro.RAR.exception.TokenException;
import com.mro.RAR.internal.parser.ResponseParsers;
import com.mro.RAR.model.QueryObjectRequest;
import com.mro.RAR.model.options.HttpMethod;
import org.apache.commons.codec.CharEncoding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mro.RAR.RARUtils.safeCloseResponse;

public class RARQueryOpeartion extends RAROperation {

    public RARQueryOpeartion(ServiceClient client, CredentialsProvider credsProvider) {
        super(client, credsProvider);
    }

    public boolean detectionCredential(Credentials credentials, boolean update) {
        CodingUtils.assertParameterNotNull(credentials, "credentials");

        String userId = credentials.getAccountId();
        String accessKey = credentials.getAccessKey();

        CodingUtils.assertParameterNotNull(userId, "userId");
        CodingUtils.assertParameterNotNull(accessKey, "accessKey");


        HashMap<String, String> params = new HashMap<>();
        params.put("ignore", "true");
        params.put("password", accessKey);
        params.put("username", userId);
        params.put("grant_type", "password");
        params.put("scope", "openid");
        params.put("client_secret", "123456");
        params.put("client_id", "mro");
        params.put("captcha", "1234");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("authorization", "Basic bXJvOjEyMzQ1Ng==");

        String authEndPoint = "http://dev.auth.ckmro.com";
        //根据endpoint判断环境 oauth
        if ("https://api.ckmro.com".equals(this.endpoint.toString())) {
            authEndPoint = "https://auth.ckmro.com";
        }

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.client))
                .setEndpoint(authEndPoint + "/oauth/token")
                .setMethod(HttpMethod.POST).setParameters(params)
                .setHeaders(headers)
                .setOriginalRequest(null).build();

        ResponseMessage responseMessage = null;
        try {
            responseMessage = this.client.sendRequest(requestMessage, new ExecutionContext());

            ResponseParsers.AuthTokenResponseParser authTokenResponseParser = responseParsers.authTokenResponseParser;
            Map<String, String> parse = authTokenResponseParser.parse(responseMessage);

            String accessToken = parse.get("access_token");

            if ("invalid_token".equals(accessToken) || "Unauthorized".equals(accessToken)) {
                return false;
            }
            if (accessToken != null) {
                if (update) {
                    credentials.setTokenStatus(true);
                    credentials.setSecurityToken(accessToken);
                    this.credsProvider.getCredentialsHandler().setSecurityToken(credentials);
                }
                return true;
            }
        } catch (ClientException var1) {
            LogUtils.logException(var1.getErrorMessage(), var1);
        } catch (ServiceException var2) {
            LogUtils.logException(var2.getErrorMessage(), var2);
        } catch (ResponseParseException var3) {
            LogUtils.logException("[ResponseParseException] companyId:" + credentials.getCompanyId()
                    + " username: " + credentials.getAccountId()
                    + " accessKey:" + credentials.getAccessKey(), var3, false);
        } finally {
            safeCloseResponse(responseMessage);
        }
        return false;
    }


    public <T> T getUserInfo(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/commons/user")
                .setMethod(HttpMethod.GET).setParameters(parameters).setHeaders(headers)
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.GetUserInfoResponseParse<T>(clazz), true, null, null);
    }

    public List<String> getOrderCloseItem(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getOrderCloseItem")
                .setMethod(HttpMethod.GET).setParameters(parameters).setHeaders(headers)
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.getOrderCloseItemResponseParser, true, null, null);
    }

    public Map getAdminInform(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getAdminInform")
                .setMethod(HttpMethod.GET).setParameters(parameters).setHeaders(headers)
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.getAdminInformResponseParser, true, null, null);
    }

    public String cancelOrder(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/cancel")
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.cancelOrderResponseParser, true, null, null);
    }

    public <T> T updateExpireInquiry(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/inquiries/updateExpire/" + parameters.get("inquiryId"))
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();
        try {
            return (T) this.doOperation(requestMessage, new ResponseParsers.UrgedOfferResponseParser<T>(clazz), true, null, null);
        } catch (Exception e) {
            LogUtils.logException(requestMessage.getEndpoint() + " message:" + e.getMessage(), e);
        }
        return null;
    }

    public <T> T urgedOffer(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/offers/urgedOffer")
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();

        try {
            return (T) this.doOperation(requestMessage, new ResponseParsers.UrgedOfferResponseParser<T>(clazz), true, null, null);
        } catch (Exception e) {
            LogUtils.logException(requestMessage.getEndpoint() + " message:" + e.getMessage(), e);
        }
        return null;
    }

    public <T> T getPaymentPlatform(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getPaymentPlatformChange")
                .setMethod(HttpMethod.GET)
                .setHeaders(headers)
                .setParameters(parameters)
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.PaymentPlatformParser<T>(clazz), true, null, null);
    }

    public <T> T cancelBillItem(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/bill/cancellation")
                .setMethod(HttpMethod.GET).setParameters(parameters).setHeaders(headers)
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.ReceiptResponseParser<T>(clazz), true, null, null);
    }

    public <T> T receiptBillItem(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/bill/receipt")
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.ReceiptResponseParser<T>(clazz), true, null, null);
    }

    public <T> T rejectOfferContract(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/reject/doc/offer")
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.confirmOrRejectOfferContractResponseParser<T>(clazz), true, null, null);

    }

    public <T> T confirmOfferContract(QueryObjectRequest request, Class<T> clazz) {
        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("cloud", "true");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/orders/contract/confirm/inquiry")
                .setMethod(HttpMethod.POST).setParameters(parameters).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(new byte[0]))
                .setOriginalRequest(request).build();
        return (T) this.doOperation(requestMessage, new ResponseParsers.confirmOrRejectOfferContractResponseParser<T>(clazz), true, null, null);
    }

    public Map getOrderStep(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "1");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getOrderStep")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters()).setHeaders(headers)
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.getOrderStepParser, true, null, null);
    }

    public Map<String, Integer> getOrderStepChange(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "0");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/loop/getOrderStepChange")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters()).setHeaders(headers)
                .setOriginalRequest(request).build();

        try {
            return this.doOperation(requestMessage, responseParsers.getOrderStepChangeParser, false, null, null);
        }catch (Exception e) {
            LogUtils.logException(requestMessage.getEndpoint() + " message:" + e.getMessage(), e);
        }
        return null;
    }

    public Map<String,Integer> getOrderChange(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "0");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/loop/getOrderChange")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters())
                .setOriginalRequest(request).build();

        try {
            return this.doOperation(requestMessage, responseParsers.getOrderChangeParser, false, null, null);
        }catch (Exception e) {
            LogUtils.logException(requestMessage.getEndpoint() + " message:" + e.getMessage(), e);
        }
        return null;
    }

    public Map getOrder(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "1");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getOrder")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters())
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.getOrderResponseParser, true, null, null);
    }

    public Map getInquiry(QueryObjectRequest request) {

        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "1");


        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getInquiry")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters())
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.getOfferResponseParser, true, null, null);
    }

    public Map getInquiryChange(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
        parameters.put("type", "0");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/loop/getInquiryChange")
                .setMethod(HttpMethod.GET).setParameters(request.getParameters())
                .setOriginalRequest(request).build();

        try {
            return this.doOperation(requestMessage, responseParsers.getInquiryChangeParser, false, null, null);
        }catch (Exception e) {
            LogUtils.logException(requestMessage.getEndpoint() + " message:" + e.getMessage(), e);
        }
        return null;
    }

    public Map<String, String> getFastTransaction(QueryObjectRequest request) {
        CodingUtils.assertParameterNotNull(request, "request");

        Map<String, String> headers = request.getHeaders();
        headers.put("Content-Type", "application/json");

        Map<String, String> parameters = request.getParameters();
//        this.credsProvider.getCredentialsHandler().getCredentials(companyId);
//        parameters.put("userName", credsProvider.getCredentials().getAccountId());

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getFastTransactionChange")
                .setMethod(HttpMethod.GET).setParameters(parameters).setHeaders(headers)
                .setOriginalRequest(request).build();

        return this.doOperation(requestMessage, responseParsers.fastTransactionResponseParse, true, null, null);
    }

    public String sureFastTransaction(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("transactionId", "null");
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/fastTransaction/sureFastTransaction")
                .setMethod(HttpMethod.GET)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .build();
        return this.doOperation(requestMessage, responseParsers.sureFastTransaction, true, null, null);
    }

    public String deleteFastTransaction(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();
        parameters.put("transactionId", "null");
        parameters.put("cloud", "true");

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/trade/fastTransaction/deleteFastTransaction")
                .setMethod(HttpMethod.GET)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .build();
        return this.doOperation(requestMessage, responseParsers.deleteFastTransaction, true, null, null);
    }

    public Integer getOrderStatus(QueryObjectRequest request) {
        Map<String, String> parameters = request.getParameters();

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/getOrderStatus")
                .setMethod(HttpMethod.GET)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .build();
        return this.doOperation(requestMessage, responseParsers.getOrderStatusParser, true, null, null);
    }

    public void executeFail(HttpMethod method, String uri,
                            Map<String, String> params, Map<String, String> headers, byte[] data) {

        InputStream inputStream = null;
        try {
            inputStream = IOUtils.stringAsInput(new String(data, CharEncoding.ISO_8859_1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(uri)
                .setMethod(method)
                .setOriginalRequest(null)
                .setParameters(params)
                .setHeaders(headers)
                .setInputStream(inputStream)
                .build();
        this.doOperation(requestMessage, responseParsers.deleteFastTransaction, true, null, null);
    }

    public String uploadFile(QueryObjectRequest request) {

        Map<String, String> parameters = request.getParameters();

        Map headers = request.getHeaders();
        headers.put("content-type", "application/json");

        RequestMessage requestMessage = (new RARRequestMessageBuilder(this.getInnerClient()))
                .setEndpoint(this.getEndpoint().toString() + "/v1/cloud/common/saasUploadFileByOss")
                .setMethod(HttpMethod.GET)
                .setOriginalRequest(request)
                .setParameters(parameters)
                .setHeaders(headers)
                .build();
        return this.doOperation(requestMessage, responseParsers.uploadFileToCkmroByOssResponseParse, false, null, null);
    }
}
