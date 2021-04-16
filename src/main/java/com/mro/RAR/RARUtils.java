package com.mro.RAR;

import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.common.utils.*;
import com.mro.RAR.model.Callback;
import com.mro.RAR.model.ObjectMetadata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RARUtils {
    public static final ResourceManager RAR_RESOURCE_MANAGER = ResourceManager.getInstance("RAR");
    public static final ResourceManager COMMON_RESOURCE_MANAGER = ResourceManager.getInstance("common");

    public RARUtils() {
    }

    public static void safeCloseRequest(RequestMessage response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException var2) {
            LogUtils.logException("Unexpected io exception when trying to close http request: ", var2);
        }

    }

    public static void safeCloseResponse(ResponseMessage response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException var2) {
            LogUtils.logException("Unexpected io exception when trying to close http response: ", var2);
        }

    }
    /**
     * Ensure the callback is valid by checking its url and body are not null or
     * empty.
     */
    public static void ensureCallbackValid(Callback callback){
        if (callback != null) {
            CodingUtils.assertStringNotNullOrEmpty(callback.getCallbackUrl(), "Callback.callbackUrl");
            CodingUtils.assertParameterNotNull(callback.getCallbackBody(), "Callback.callbackBody");
        }
    }

    /**
     * Populate metadata to headers.
     */
    public static void populateRequestMetadata(Map<String, String> headers, ObjectMetadata metadata) {
        Map<String, Object> rawMetadata = metadata.getRawMetadata();
        if (rawMetadata != null) {
            for (Map.Entry<String, Object> entry : rawMetadata.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (key != null)
                        key = key.trim();
                    if (value != null)
                        value = value.trim();
                    headers.put(key, value);
                }
            }
        }

        Map<String, String> userMetadata = metadata.getUserMetadata();
        if (userMetadata != null) {
            for (Map.Entry<String, String> entry : userMetadata.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key != null)
                        key = key.trim();
                    if (value != null)
                        value = value.trim();
                    headers.put(RARHeaders.RAR_USER_METADATA_PREFIX + key, value);
                }
            }
        }
    }

    /**
     * Encode the callback with JSON.
     */
    public static String jsonizeCallback(Callback callback) {
        StringBuffer jsonBody = new StringBuffer();

        jsonBody.append("{");
        // url, required
        jsonBody.append("\"callbackUrl\":" + "\"" + callback.getCallbackUrl() + "\"");

        // host, optional
        if (callback.getCallbackHost() != null && !callback.getCallbackHost().isEmpty()) {
            jsonBody.append(",\"callbackHost\":" + "\"" + callback.getCallbackHost() + "\"");
        }

        // body, require
        jsonBody.append(",\"callbackBody\":" + "\"" + callback.getCallbackBody() + "\"");

        // bodyType, optional
        if (callback.getCalbackBodyType() == Callback.CalbackBodyType.JSON) {
            jsonBody.append(",\"callbackBodyType\":\"application/json\"");
        } else if (callback.getCalbackBodyType() == Callback.CalbackBodyType.URL) {
            jsonBody.append(",\"callbackBodyType\":\"application/x-www-form-urlencoded\"");
        }
        jsonBody.append("}");

        return jsonBody.toString();
    }

    /**
     * Encode CallbackVar with Json.
     */
    public static String jsonizeCallbackVar(Callback callback) {
        StringBuffer jsonBody = new StringBuffer();

        jsonBody.append("{");
        for (Map.Entry<String, String> entry : callback.getCallbackVar().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                if (!jsonBody.toString().equals("{")) {
                    jsonBody.append(",");
                }
                jsonBody.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\" ");
            }
        }
        jsonBody.append("}");

        return jsonBody.toString();
    }

    /**
     * Put the callback parameter into header.
     */
    public static void populateRequestCallback(Map<String, String> headers, Callback callback) {
        if (callback != null) {
            String jsonCb = jsonizeCallback(callback);
            String base64Cb = BinaryUtil.toBase64String(jsonCb.getBytes());

            headers.put(RARHeaders.RAR_HEADER_CALLBACK, base64Cb);

            if (callback.hasCallbackVar()) {
                String jsonCbVar = jsonizeCallbackVar(callback);
                String base64CbVar = BinaryUtil.toBase64String(jsonCbVar.getBytes());
                base64CbVar = base64CbVar.replaceAll("\n", "").replaceAll("\r", "");
                headers.put(RARHeaders.RAR_HEADER_CALLBACK_VAR, base64CbVar);
            }
        }
    }

    public static void removeHeader(Map<String, String> headers, String header) {
        if (header != null && headers.containsKey(header)) {
            headers.remove(header);
        }
    }

    public static void ensureObjectKeyValid(String key) {
        if (!validateObjectKey(key)) {
            throw new IllegalArgumentException(RAR_RESOURCE_MANAGER.getFormattedString("ObjectKeyInvalid", key));
        }
    }

    /**
     * 验证文件名称
     */
    public static boolean validateObjectKey(String key) {

        if (key == null || key.length() == 0) {
            return false;
        }

        byte[] bytes = null;
        try {
            bytes = key.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        // Validate exculde xml unsupported chars
        char keyChars[] = key.toCharArray();
        char firstChar = keyChars[0];
        if (firstChar == '/' || firstChar == '\\') {
            return false;
        }

        return (bytes.length > 0 && bytes.length < 1024);
    }
}
