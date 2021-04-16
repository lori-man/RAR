package com.mro.RAR;

import com.mro.RAR.auth.RequestSigner;
import com.mro.RAR.common.SignVersion;
import com.mro.RAR.common.utils.ResourceManager;
import com.mro.RAR.common.utils.VersionInfoUtils;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.model.options.Protocol;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RAR请求响应头配置
 */
public class ClientConfiguration {

    public static final String DEFAULT_USER_AGENT = VersionInfoUtils.getDefaultUserAgent();

    //默认配置
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = -1;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 50000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 50000;
    public static final int DEFAULT_MAX_CONNECTIONS = 1024;
    public static final long DEFAULT_CONNECTION_TTL = -1L;
    public static final long DEFAULT_IDLE_CONNECTION_TIME = 60000L;
    public static final int DEFAULT_VALIDATE_AFTER_INACTIVITY = 2000;
    public static final int DEFAULT_THREAD_POOL_WAIT_TIME = 60000;
    public static final int DEFAULT_REQUEST_TIMEOUT = 300000;
    public static final long DEFAULT_SLOW_REQUESTS_THRESHOLD = 300000L;
    public static final boolean DEFAULT_USE_REAPER = true;
    public static final SignVersion DEFAULT_SIGNATURE_VERSION;

    protected String userAgent;
    protected int maxErrorRetry;
    protected int connectionRequestTimeout;
    protected int connectionTimeout;
    protected int socketTimeout;
    protected int maxConnections;
    protected long connectionTTL;
    protected boolean useReaper = DEFAULT_USE_REAPER;
    protected long idleConnectionTime;

    protected Protocol protocol;

    protected String proxyHost;
    protected int proxyPort;
    protected String proxyUsername;
    protected String proxyPassword;
    protected String proxyDomain;
    protected String proxyWorkstation;

    protected boolean supportCname;
    protected List<String> cnameExcludeList;
    protected Lock rlock;

    protected boolean sldEnabled;

    protected int requestTimeout;
    protected boolean requestTimeoutEnabled;
    protected long slowRequestsThreshold;

    protected Map<String, String> defaultHeaders;

    protected boolean crcCheckEnabled;

    protected List<RequestSigner> signerHandlers;

    protected SignVersion signatureVersion;

    protected long tickOffset;

    public ClientConfiguration() {
        this.userAgent = DEFAULT_USER_AGENT;
        this.maxErrorRetry = DEFAULT_MAX_RETRIES;
        this.connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
        this.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        this.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
        this.maxConnections = DEFAULT_MAX_CONNECTIONS;
        this.connectionTTL = DEFAULT_CONNECTION_TTL;
        this.idleConnectionTime = DEFAULT_IDLE_CONNECTION_TIME;

        this.protocol = Protocol.HTTP;

        this.proxyHost = null;
        this.proxyPort = -1;
        this.proxyUsername = null;
        this.proxyPassword = null;
        this.proxyDomain = null;
        this.proxyWorkstation = null;

        this.supportCname = false; //后续添加cname再使用
        this.cnameExcludeList = new ArrayList<>();
        this.rlock = new ReentrantLock();

        this.sldEnabled = false;

        this.requestTimeout = DEFAULT_REQUEST_TIMEOUT;
        this.requestTimeoutEnabled = false;
        this.slowRequestsThreshold = DEFAULT_SLOW_REQUESTS_THRESHOLD;

        this.defaultHeaders = new LinkedHashMap();

        this.crcCheckEnabled = true;

        this.signerHandlers = new LinkedList();

        this.signatureVersion = DEFAULT_SIGNATURE_VERSION;

        this.tickOffset = 0L;
    }

    /**
     * 获取用户代理字符串。
     */
    public String getUserAgent() {
        return this.userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 获取代理主机
     * @return
     */
    public String getProxyHost() {
        return this.proxyHost;
    }
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * 端口
     */
    public int getProxyPort() {
        return this.proxyPort;
    }
    public void setProxyPort(int proxyPort) throws ClientException {
        if (proxyPort <= 0) {
            throw new ClientException(ResourceManager.getInstance("common").getString("ParameterIsInvalid"), (Throwable)null);
        } else {
            this.proxyPort = proxyPort;
        }
    }

    /**
     * 获取代理用户名
     */
    public String getProxyUsername() {
        return this.proxyUsername;
    }
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyDomain() {
        return this.proxyDomain;
    }
    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }

    /**
     * 获取代理主机的NTLM身份验证服务器。
     */
    public String getProxyWorkstation() {
        return this.proxyWorkstation;
    }

    /**
     * 设置代理主机的NTLM身份验证服务器(可选，如果代理服务器不需要NTLM身份验证，因此不需要)。
     */
    public void setProxyWorkstation(String proxyWorkstation) {
        this.proxyWorkstation = proxyWorkstation;
    }

    /**
     * 获取最大连接数
     * 当连接数达到最大值后，系统会继续接收连接但不会超过 acceptCount的值
     */
    public int getMaxConnections() {
        return this.maxConnections;
    }
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * 获取套接字超时，以毫秒为单位。0表示无限超时，不推荐使用0
     * 客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
     */
    public int getSocketTimeout() {
        return this.socketTimeout;
    }
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 获取套接字连接超时，以毫秒为单位。
     * 客户端和服务器建立连接的timeout 就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。
     * 超时后会ConnectionTimeOutException
     */
    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * 获取检索可用连接的超时(以毫秒为单位)来自连接管理器。0表示无穷大-1表示没有定义。默认是-1。
     * 从连接池获取连接的timeout
     */
    public int getConnectionRequestTimeout() {
        return this.connectionRequestTimeout;
    }
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    /**
     * 获取可重试错误的最大重试计数。默认情况下是3。
     */
    public int getMaxErrorRetry() {
        return this.maxErrorRetry;
    }
    public void setMaxErrorRetry(int maxErrorRetry) {
        this.maxErrorRetry = maxErrorRetry;
    }

    /**
     * 获取是否管理httpClientManager连接
     */
    public boolean isUseReaper() {
        return useReaper;
    }
    public void setUseReaper(boolean useReaper) {
        this.useReaper = useReaper;
    }

    /**
     * 获取连接的最大空闲时间。如果连接一直处于空闲状态,超过这个数字，它将关闭。
     */
    public long getIdleConnectionTime() {
        return this.idleConnectionTime;
    }
    public void setIdleConnectionTime(long idleConnectionTime) {
        this.idleConnectionTime = idleConnectionTime;
    }

    /**
     * 获取OSS的协议(HTTP或HTTPS),默认HTTP
     */
    public Protocol getProtocol() {
        return this.protocol;
    }
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * 是否使用二级域名,默认是false
     * @return
     */
    public boolean isSLDEnabled() {
        return this.sldEnabled;
    }
    public ClientConfiguration setSLDEnabled(boolean enabled) {
        this.sldEnabled = enabled;
        return this;
    }

    /**
     * 触发的连接空闲时间阈值(以毫秒为单位),验证。默认情况下是2000。
     */
    public int getValidateAfterInactivity() {
        return 2000;
    }

    /**
     * 获取启用请求超时的标志。默认情况下它是禁用的。
     */
    public boolean isRequestTimeoutEnabled() {
        return this.requestTimeoutEnabled;
    }
    public void setRequestTimeoutEnabled(boolean requestTimeoutEnabled) {
        this.requestTimeoutEnabled = requestTimeoutEnabled;
    }

    /**
     * 获取超时值，以毫秒为单位。
     * 请求超时时间
     */
    public int getRequestTimeout() {
        return this.requestTimeout;
    }
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    /**
     * 设置缓慢请求的延迟阈值。如果请求的延迟时间更长,超过它，请求将被记录。默认的阈值是5分钟。
     */
    public long getSlowRequestsThreshold() {
        return this.slowRequestsThreshold;
    }
    public void setSlowRequestsThreshold(long slowRequestsThreshold) {
        this.slowRequestsThreshold = slowRequestsThreshold;
    }

    /**
     * 获取默认http头。所有这些标题都是自动的,在每个请求中添加。如果请求中也指定了header，默认将被覆盖。
     */
    public Map<String, String> getDefaultHeaders() {
        return this.defaultHeaders;
    }
    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }
    public void addDefaultHeader(String key, String value) {
        this.defaultHeaders.put(key, value);
    }

    /**
     * 获取在上传和下载时启用CRC校验和的标志。默认情况下true
     */
    public boolean isCrcCheckEnabled() {
        return this.crcCheckEnabled;
    }
    public void setCrcCheckEnabled(boolean crcCheckEnabled) {
        this.crcCheckEnabled = crcCheckEnabled;
    }

    /**
     * 得到signer handlers
     */
    public List<RequestSigner> getSignerHandlers() {
        return this.signerHandlers;
    }
    public void setSignerHandlers(List<RequestSigner> signerHandlers) {
        if (signerHandlers == null) {
            return;
        }
        this.signerHandlers.clear();
        for (RequestSigner signer : signerHandlers) {
            if (signer != null) {
                this.signerHandlers.add(signer);
            }
        }
    }

    /**
     * 获取signer 版本
     * @return
     */
    public SignVersion getSignatureVersion() {
        return this.signatureVersion;
    }
    public void setSignatureVersion(SignVersion signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    /**
     * 获取自定义纪元时间与本地时间之间的差异(以毫秒为单位)。
     *  RAR token是有时间验证的
     */
    public long getTickOffset() {
        return this.tickOffset;
    }
    public void setTickOffset(long epochTicks) {
        long localTime = (new Date()).getTime();
        this.tickOffset = epochTicks - localTime;
    }

    static {
        DEFAULT_SIGNATURE_VERSION = SignVersion.V1;
    }
}