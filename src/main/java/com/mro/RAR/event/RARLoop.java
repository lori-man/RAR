package com.mro.RAR.event;

public class RARLoop {

    private LoopHandler loopHandler;
    private Thread loopThread;

    private volatile boolean keepAlive = true;

    public RARLoop(LoopHandler loopHandler) {
        httpLoop(loopHandler);
    }

    private void httpLoop(LoopHandler loopHandler) {
        this.loopHandler = loopHandler;
        this.loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpLongConnLoop(loopHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"RARLoop");
        this.loopThread.start();
    }

    private void httpLongConnLoop(LoopHandler loopHandler) throws Exception {

        while (keepAlive) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            if (loopHandler != null) {
                loopHandler.LoopStrategy();
            }

            Thread.sleep(60 * 1000);
        }
    }

    /**
     * 重新开启Loop
     */
    public boolean openLoop(LoopHandler loopHandler) {
        if (loopHandler == null) {
            return false;
        }

        if (keepAlive) {
            this.closeLoop();
        }
        this.keepAlive = true;
        this.loopHandler = loopHandler;
        httpLoop(loopHandler);
        return this.keepAlive;
    }

    /**
     * 轮询线程是否alive
     * @return
     */
    public boolean isLoop() {
        return this.loopThread.isAlive() && keepAlive;
    }

    /**
     * 关闭
     */
    public void closeLoop() {
        if (this.loopThread != null) {
            this.loopThread.interrupt();
        }
        this.keepAlive = false;
    }

}
