package com.mro.RAR.common.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * sdk版本信息 工具类
 */
public class VersionInfoUtils {

    private static final String VERSION_INFO_FILE = "versioninfo.properties";
    private static final String USER_AGENT_PREFIX = "mro-sdk-java";
    private static String version = null;
    private static String defaultUserAgent = null;

    public VersionInfoUtils() {
    }

    public static String getVersion() {
        if (version == null) {
            initializeVersion();
        }

        return version;
    }

    public static String getDefaultUserAgent() {
        if (defaultUserAgent == null) {
            defaultUserAgent = "mro-sdk-java/" + getVersion() + "(" + System.getProperty("os.name") + "/" + System.getProperty("os.version") + "/" + System.getProperty("os.arch") + ";" + System.getProperty("java.version") + ")";
        }

        return defaultUserAgent;
    }

    private static void initializeVersion() {
        InputStream inputStream = VersionInfoUtils.class.getClassLoader().getResourceAsStream("versioninfo.properties");
        Properties versionInfoProperties = new Properties();

        try {
            if (inputStream == null) {
                throw new IllegalArgumentException("versioninfo.properties not found on classpath");
            }

            versionInfoProperties.load(inputStream);
            version = versionInfoProperties.getProperty("version");
        } catch (Exception var3) {
            //记录日志

            //更改version
            version = "unknown-version";
        }

    }
}
