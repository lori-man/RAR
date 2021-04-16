package com.mro.RAR.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Http 工具类
 */
public class HttpUtil {
    private static final String ISO_8859_1_CHARSET = "iso-8859-1";
    private static final String UTF8_CHARSET = "utf-8";

    public HttpUtil() {
    }



    public static void convertHeaderCharsetFromIso88591(Map<String, String> headers) {
        convertHeaderCharset(headers, "iso-8859-1", "utf-8");
    }

    public static void convertHeaderCharsetToIso88591(Map<String, String> headers) {
        convertHeaderCharset(headers, "utf-8", "iso-8859-1");
    }

    /**
     * 转换字符编码
     * @param headers
     * @param fromCharset
     * @param toCharset
     */
    private static void convertHeaderCharset(Map<String, String> headers, String fromCharset, String toCharset) {
        Iterator i$ = headers.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> header = (Map.Entry)i$.next();
            if (header.getValue() != null) {
                try {
                    header.setValue(new String(((String)header.getValue()).getBytes(fromCharset), toCharset));
                } catch (UnsupportedEncodingException var6) {
                    throw new IllegalArgumentException("Invalid charset name: " + var6.getMessage(), var6);
                }
            }
        }

    }

    /**
     * 将参数param转换成URN
     * @param params
     * @param charset
     * @return
     */
    public static String paramToQueryString(Map<String, String> params, String charset) {
        if (params != null && !params.isEmpty()) {
            StringBuilder paramString = new StringBuilder();
            boolean first = true;

            for(Iterator i$ = params.entrySet().iterator(); i$.hasNext(); first = false) {
                Map.Entry<String, String> p = (Map.Entry)i$.next();
                String key = (String)p.getKey();
                String value = (String)p.getValue();
                if (!first) {
                    paramString.append("&");
                }

                paramString.append(urlEncode(key, charset));
                if (value != null) {
                    paramString.append("=").append(urlEncode(value, charset));
                }
            }

            return paramString.toString();
        } else {
            return null;
        }
    }

    public static String urlEncode(String value, String encoding) {
        if (value == null) {
            return "";
        } else {
            try {
                String encoded = URLEncoder.encode(value, encoding);
                return encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
            } catch (UnsupportedEncodingException var3) {
                throw new UnsupportedOperationException();
            }
        }
    }
}

