package com.mro.RAR.common;

import com.mro.RAR.common.utils.LogUtils;
import org.apache.http.conn.HttpClientConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用于定期检查连接池是否空闲连接的守护进程线程
 */
public class IdleConnectionReaper extends Thread {
    private static final int REAP_INTERVAL_MILLISECONDS = 5 * 1000;

    private static final ArrayList<HttpClientConnectionManager> connectionManagers = new ArrayList<>();

    private static IdleConnectionReaper instance;
    private static long idleConnectionTime = 60 * 1000;

    private volatile boolean shuttingDown;


    private IdleConnectionReaper() {
        super("idle_connection_reaper");
        setDaemon(true);
    }

    public static synchronized boolean registerConnectionManager(HttpClientConnectionManager connectionManager) {
        if (instance == null) {
            instance = new IdleConnectionReaper();
            instance.start();
        }
        return connectionManagers.add(connectionManager);
    }

    public static synchronized boolean removeConnectionManager(HttpClientConnectionManager connectionManager) {
        boolean remove = connectionManagers.remove(connectionManager);
        if (connectionManagers.isEmpty()) {
            shutdown();
        }
        return remove;
    }

    private void markShuttingDown() {
        shuttingDown = true;
    }

    public static synchronized boolean shutdown() {
        if (instance != null) {
            instance.markShuttingDown();
            instance.interrupt();
            connectionManagers.clear();
            instance = null;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        while (true) {
            if (shuttingDown) {
                LogUtils.logInfo("Shutting down reaper thread.");
                return;
            }

            try {
                sleep(REAP_INTERVAL_MILLISECONDS);
            } catch (InterruptedException e) {
            }

            try {
                List<HttpClientConnectionManager> connectionManagers = null;
                synchronized (IdleConnectionReaper.class) {
                    connectionManagers = (List<HttpClientConnectionManager>) IdleConnectionReaper.connectionManagers.clone();
                }

                for (HttpClientConnectionManager manager : connectionManagers) {
                    try {
                        manager.closeExpiredConnections();
                        manager.closeIdleConnections(idleConnectionTime, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        LogUtils.logException("Unable to close idle connections", e);
                    }
                }
            } catch (Throwable throwable) {
                LogUtils.logException("Reaper thread: ", throwable);
            }
        }
    }

    public static synchronized int size() {
        return connectionManagers.size();
    }

    public static synchronized void setIdleConnectionTime(long idletime) {
        idleConnectionTime = idletime;
    }
}
