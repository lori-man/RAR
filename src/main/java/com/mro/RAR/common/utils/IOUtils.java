package com.mro.RAR.common.utils;

import java.io.*;

public class IOUtils {
    public IOUtils() {
    }

    /**
     * io流转byte数组
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readStreamAsByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            boolean var3 = true;

            int len;
            while((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            output.flush();
            return output.toByteArray();
        }
    }

    /**
     * InputStream关闭
     * @param inputStream
     */
    public static void safeClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    /**
     * OutputStream关闭
     * @param outputStream
     */
    public static void safeClose(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    public static InputStream stringAsInput(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }
}

