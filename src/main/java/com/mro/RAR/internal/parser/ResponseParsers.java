package com.mro.RAR.internal.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mro.RAR.RARUtils;
import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.common.utils.IOUtils;
import com.mro.RAR.exception.ForbiddenException;
import com.mro.RAR.exception.InvalidTokenException;
import com.mro.RAR.exception.ResponseParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * response解析器工厂
 */
public class ResponseParsers {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public final AuthTokenResponseParser authTokenResponseParser = new AuthTokenResponseParser();
    public final JtiResponseParser jtiResponseParser = new JtiResponseParser();
    public final SaveInquiryResponseParser saveInquiryResponseParser = new SaveInquiryResponseParser();
    public final UpdateInquiryResponseParser updateInquiryResponseParser = new UpdateInquiryResponseParser();
    public final GetInquiryChangeParser getInquiryChangeParser = new GetInquiryChangeParser();
    public final GetOfferResponseParser getOfferResponseParser = new GetOfferResponseParser();
    public final GetOrderChangeParser getOrderChangeParser = new GetOrderChangeParser();
    public final GetOrderResponseParser getOrderResponseParser = new GetOrderResponseParser();
    public final GetOrderStepChangeParser getOrderStepChangeParser = new GetOrderStepChangeParser();
    public final GetOrderStepParser getOrderStepParser = new GetOrderStepParser();
    public final UpdateStockTableIdResponseParser updateStockTableIdResponseParser = new UpdateStockTableIdResponseParser();
    public final UpdateShouldBillResponseParser updateShouldBillResponseParser = new UpdateShouldBillResponseParser();
    public final CancelOrderResponseParser cancelOrderResponseParser = new CancelOrderResponseParser();
    public final GetOrderCloseItemResponseParser getOrderCloseItemResponseParser = new GetOrderCloseItemResponseParser();
    public final GetAdminInformResponseParser getAdminInformResponseParser = new GetAdminInformResponseParser();
    public final UpdateSuggestItemResponseParse updateSuggestItemResponseParse = new UpdateSuggestItemResponseParse();
    public final GetFastTransactionResponseParse fastTransactionResponseParse = new GetFastTransactionResponseParse();
    public final RequestFastTransaction requestFastTransaction = new RequestFastTransaction();
    public final SureFastTransaction sureFastTransaction = new SureFastTransaction();
    public final DeleteFastTransaction deleteFastTransaction = new DeleteFastTransaction();
    public final GetOrderStatusParser getOrderStatusParser = new GetOrderStatusParser();
    public final ExecuteFailResponseParse executeFailResponseParse = new ExecuteFailResponseParse();
    public final uploadFileToCkmroByOssResponseParse uploadFileToCkmroByOssResponseParse = new uploadFileToCkmroByOssResponseParse();
    public final InitiateMultipartUploadResponseParser initiateMultipartUploadResponseParser = new InitiateMultipartUploadResponseParser();

    public static final class InitiateMultipartUploadResponseParser implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "InitiateMultipartUpload fail");
            String data = (String) map.get("data");
            return data;
        }
    }

    public static final class uploadFileToCkmroByOssResponseParse implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "uploadFile fail");
            String data = (String) map.get("data");
            return data;
        }
    }

    public static final class ExecuteFailResponseParse implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "ExecuteFail fail");
            return "";
        }
    }

    public static final class DeleteFastTransaction implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "RequestFastTransaction fail");
            return "";
        }
    }

    public static final class GetOrderStatusParser implements ResponseParser<Integer> {
        @Override
        public Integer parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "GetOrderStatusParser fail");

            int data = (int) map.get("data");

            return data;
        }
    }


    public static final class SureFastTransaction implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "SureFastTransaction fail");
            return "";
        }
    }

    public static final class RequestFastTransaction implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "RequestFastTransaction fail");
            return "";
        }
    }

    public static final class GetFastTransactionResponseParse implements ResponseParser<Map<String, String>> {
        @Override
        public Map<String,String> parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "GetFastTransaction fail");

            if (map == null) {
                return null;
            }

            return (Map<String, String>) map.get("data");
        }
    }


    public static final class UpdateSuggestItemResponseParse implements ResponseParser<String>{
        public UpdateSuggestItemResponseParse() {
        }

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "UpdateSuggestItemResponseParse fail");
            return null;
        }
    }

    public static final class GetUserInfoResponseParse<T> implements ResponseParser{
        private Class<T> model;

        public GetUserInfoResponseParse() {
        }

        public GetUserInfoResponseParse(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "UrgedOffer fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");

            if (data == null) {
                return null;
            }

            try {
                String s = objectMapper.writeValueAsString(data);
                JavaType javaType = objectMapper.getTypeFactory().constructType(model);
                return objectMapper.readValue(s, javaType);
            }  catch (IOException e) {
                throw new ResponseParseException("GetUserInfoResponseParse transferm " + model + " fail");
            }
        }
    }

    public static final class GetOrderCloseItemResponseParser implements ResponseParser<List<String>>{

        public GetOrderCloseItemResponseParser() {
        }

        @Override
        public List<String> parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "update ckmro PlatCompany fail");

            if (map == null) {
                return null;
            }

            List data = (List) map.get("data");

            return data;
        }
    }

    public static final class GetAdminInformResponseParser implements ResponseParser<Map>{

        public GetAdminInformResponseParser() {
        }

        @Override
        public Map parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "update ckmro PlatCompany fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");

            return data;
        }
    }

    public static final class UpdatePlatCompanyResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public UpdatePlatCompanyResponseParser() {
        }

        public UpdatePlatCompanyResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "update ckmro PlatCompany fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UpdateCkmroBillResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public UpdateCkmroBillResponseParser() {
        }

        public UpdateCkmroBillResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "update ckmro bill fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class CancelOrderResponseParser implements ResponseParser<String> {

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "cancel order fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UrgedOfferResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public UrgedOfferResponseParser() {
        }

        public UrgedOfferResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "UrgedOffer fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");
            return null;
        }
    }

    public static final class PaymentPlatformParser<T> implements ResponseParser{
        private Class<T> model;

        public PaymentPlatformParser() {
        }

        public PaymentPlatformParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire PaymentPlatform fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");
            return convertClass(data, model, "paymentPlatform", "PaymentPlatformParser");
        }
    }

    public static final class ReceiptResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public ReceiptResponseParser() {
        }

        public ReceiptResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "confirm or reject billItem fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UpdateShouldBillResponseParser implements ResponseParser<String> {

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "UpdateShouldBill fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UpdateBillItemResponseParser implements ResponseParser<String>{

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "updateBillItem fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class StockingResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public StockingResponseParser() {
        }

        public StockingResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "confirm stocking");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UpdateStockTableIdResponseParser implements ResponseParser<String> {

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "ckmro update stockTable fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }
    public static final class InsertPaymentItem<T> implements ResponseParser{
        private Class<T> model;

        public InsertPaymentItem() {
        }

        public InsertPaymentItem(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "ckmro payment fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }


    public static final class confirmOrRejectOfferContractResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public confirmOrRejectOfferContractResponseParser() {
        }

        public confirmOrRejectOfferContractResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "confirm or reject offer fail");

            if (map == null) {
                return map;
            }

            return null;
        }
    }

    public static final class UploadDocResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public UploadDocResponseParser() {
        }

        public UploadDocResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "ckmro upload doc fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public static final class UploadInquiryDocResponseParser<T> implements ResponseParser{
        private Class<T> model;

        public UploadInquiryDocResponseParser() {
        }

        public UploadInquiryDocResponseParser(Class<T> clazz) {
            this.model = clazz;
        }

        @Override
        public Object parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "ckmro upload inquiry doc fail");

            if (map == null) {
                return map;
            }

            return null;
        }
    }

    public final class GetOrderStepParser implements ResponseParser<Map> {

        @Override
        public Map parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire orderStep fail");

            if (map == null) {
                return map;
            }

            Map data = (Map) map.get("data");
            return data;
        }
    }

    public final class GetOrderStepChangeParser implements ResponseParser<Map<String, Integer>> {

        @Override
        public Map<String, Integer> parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire orderStep change fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");
            return (Map<String, Integer>) data;
        }
    }

    public final class GetOrderChangeParser implements ResponseParser<Map<String, Integer>> {

        @Override
        public Map<String, Integer> parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire orderStep change fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");
            return (Map<String, Integer>) data;
        }
    }

    public final class GetInquiryChangeParser implements ResponseParser<Map> {

        @Override
        public Map parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire offer change fail");

            if (map == null) {
                return null;
            }
            Map data = (Map) map.get("data");
            return data;
        }
    }

    public static final class GetOrderResponseParser implements ResponseParser<Map> {

        @Override
        public Map parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire order fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");

            return data;
        }
    }

    public static final class GetOfferResponseParser implements ResponseParser<Map> {

        public Map parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "acquire offer fail");

            if (map == null) {
                return null;
            }

            Map data = (Map) map.get("data");

            return data;

/*            //在String前添加 class地址
//            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            try (InputStream content = var1.getContent();){
                byte[] bytes = IOUtils.readStreamAsByteArray(content);
                String s = new String(bytes);
                Map map = objectMapper.readValue(s, Map.class);
                Map data = (Map) map.get("data");
                Map change = (Map) data.get("offer");
                if (change != null) {
                    s = objectMapper.writeValueAsString(change);
                    JavaType javaType = objectMapper.getTypeFactory().constructType(this.model);
                    return objectMapper.readValue(s, javaType);
                }
            } catch (IOException e) {
                LogUtils.logException("Offer ResponseMessage transform fail", e);
            }finally {
                RARUtils.safeCloseResponse(var1);
            }
            return null;*/
        }
    }

    public static final class CreateOrderResponseParser<T> implements ResponseParser {
        private Class<T> model;

        public CreateOrderResponseParser() {
        }

        public CreateOrderResponseParser(Class<T> clazz) {
            this.model = clazz;
        }
        public void setData(Class<T> data) {
            this.model = data;
        }

        public List<T> parse(ResponseMessage var1) throws ResponseParseException {
            Map map=parserCode(var1,"create ckmro oreder fail");

            if (map == null) {
                return null;
            }

//            List<Map> data = (List<Map>) map.get("data");
//            List<T> orders = new ArrayList<>();
//            data.stream().forEach(orderMap -> {
//                try {
//                    String resultValue = objectMapper.writeValueAsString(orderMap);
//                    JavaType javaType = objectMapper.getTypeFactory().constructType(this.model);
//                    T o = (T) objectMapper.readValue(resultValue, javaType);
//                    orders.add(o);
//                } catch (JsonMappingException e) {
//                    LogUtils.logException("string to json 转换失败", e);
//                } catch (JsonProcessingException e) {
//                    LogUtils.logException("onject to string 转换失败", e);
//                }
//            });
//            return orders.size() > 0 ? orders : null;
            return null;
        }
    }

    public final class UpdateInquiryResponseParser implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "update inquiry fail");

            if (map == null) {
                return null;
            }

            return null;
        }
    }

    public final class SaveInquiryResponseParser implements ResponseParser<String> {
        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            Map map = parserCode(var1, "save inquiry fail");

            if (map == null) {
                return null;
            }

            Map<String, String> data = (Map<String, String>) map.get("data");

            if (data == null) {
                return null;
            }

            return data.get("entryId");
        }
    }


    public final class AuthTokenResponseParser implements ResponseParser<Map<String, String>> {
        @Override
        public Map<String, String> parse(ResponseMessage var1) throws ResponseParseException {
            if (var1.getStatusCode() / 100 != 2) {
                throw new ResponseParseException("AuthTokenResponseParser code:" + var1.getStatusCode() + ",message:" + var1.getErrorResponseAsString());
            }
            try (InputStream content = var1.getContent();) {
                byte[] bytes = IOUtils.readStreamAsByteArray(content);
                Map<String, String> map = (Map<String, String>) objectMapper.readValue(bytes, Map.class);
                Map<String, String> result = new HashMap<>();
                String error = map.get("error");
                if ("invalid_token".equals(error)) {
                    throw new ResponseParseException("invalid_token,please request again");
                }
                if ("Unauthorized".equals(error)) {
                    throw new ResponseParseException("Unauthorized,please request again");
                }
                if ("unauthorized".equals(error)) {
                    throw new ResponseParseException("Unauthorized,please request again");
                }

                String accessToken = map.get("access_token");
                String jti = map.get("jti");
//                Asserts.notNull(accessToken, "access_token");
//                Asserts.notNull(jti, "jti");
                result.put("access_token", accessToken);
                result.put("jti", jti);
                return result;
            } catch (IOException e) {
                throw new ResponseParseException("AuthTokenResponseParser IO transform byte arrays fail");
            } finally {
                RARUtils.safeCloseResponse(var1);
            }
        }
    }

    public final class JtiResponseParser implements ResponseParser<String>{

        @Override
        public String parse(ResponseMessage var1) throws ResponseParseException {
            try (InputStream content = var1.getContent();) {
                byte[] bytes = IOUtils.readStreamAsByteArray(content);

                return "ok";
            } catch (IOException e) {
                throw new ResponseParseException("JtiResponseParser IO transform byte arrays fail");
            } finally {
                RARUtils.safeCloseResponse(var1);
            }
        }
    }


    public final class EmptyResponseParser implements ResponseParser<ResponseMessage> {
        public ResponseMessage parse(ResponseMessage response) {
            RARUtils.safeCloseResponse(response);
            return response;
        }
    }

    public static Map parserCode(ResponseMessage var1, String errorMessage) throws ResponseParseException {
        if (var1 == null) {
            RARUtils.safeCloseResponse(var1);
            throw new ResponseParseException("return ResponseMessage is null");
        }

        try (InputStream content = var1.getContent();) {
            if (var1.getStatusCode() / 100 != 2) {
                if (var1.getErrorResponseAsString() != null) {
                    String errorResponseAsString = var1.getErrorResponseAsString();
                    Map map = null;
                    try {
                        map = objectMapper.readValue(errorResponseAsString, Map.class);
                    } catch (IOException e) {
//                        LogUtils.logInfo("ResponseMessage io tranfer fail errorResponseAsString ");
                    }
                    if (map != null) {
                        if ("invalid_token".equals(map.get("error")) || "unauthorized".equals(map.get("error"))) {
                            throw new InvalidTokenException("invalid_token");
                        }
                        if ("FORBIDDEN".equals(map.get("error"))) {
                            throw new ForbiddenException("FORBIDDEN");
                        }
                    }
                }
                throw new ResponseParseException(errorMessage + " code:" + var1.getStatusCode() + " message:" + var1.getErrorResponseAsString());
            }
            byte[] bytes = IOUtils.readStreamAsByteArray(content);
            String s = new String(bytes);
            if (s == null) {
                return null;
            }
            Map map = objectMapper.readValue(s, Map.class);
            if (map.get("code") != null && Integer.valueOf(map.get("code").toString()) / 100 != 2) {
                if (map.get("message") != null) {
                    String message = map.get("message").toString();
                    Map messageMap = null;
                    try {
                        messageMap = objectMapper.readValue(message, Map.class);
                    } catch (IOException e) {
//                        LogUtils.logInfo("ResponseMessage io tranfer fail message ");
                    }
                    if (messageMap != null) {
                        if ("invalid_token".equals(messageMap.get("error")) || "unauthorized".equals(messageMap.get("error"))) {
                            throw new InvalidTokenException("invalid_token");
                        }
                        if ("FORBIDDEN".equals(messageMap.get("error"))) {
                            throw new ForbiddenException("FORBIDDEN");
                        }
                    }
                }
                throw new ResponseParseException(errorMessage + " code:" + Integer.valueOf(map.get("code").toString()) + " message:" + map.get("data"));
//                throw new ResponseParseException(errorMessage + " code:" + Integer.valueOf(map.get("code").toString()) + " message:" + map.get("message"));
            }
            return map;
        } catch (IOException e) {
            throw new ResponseParseException("ResponseMessage io tranfer fail ");
        } finally {
            RARUtils.safeCloseResponse(var1);
        }
    }

    public static <T> T convertClass(Map data, Class<T> clazz, String key, String parser) throws ResponseParseException {
        if (data == null) {
            return null;
        }
        Map keyMap = (Map) data.get(key);

        if (keyMap == null) {
            return null;
        }

        try {
            String s = objectMapper.writeValueAsString(keyMap);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
            return objectMapper.readValue(s, javaType);
        } catch (IOException e) {
            throw new ResponseParseException(parser + " transorm " + clazz + " fail");
        }
    }
}
