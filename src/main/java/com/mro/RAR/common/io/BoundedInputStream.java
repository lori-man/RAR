package com.mro.RAR.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 这是一个只提供特定长度的字节的流:如果它的位置超过了指定长度，它就会停止。
 *
 * 当从ServletInputStream中读取不存在的内容，则ServletInputStream将会阻塞，
 * 因为它不知道内容是否还没有到达或内容是否已经完成。因此我们需在content -length进行了初始化，
 * 它将停止阻塞，前提是发送了正确的内容长度。
 */
public class BoundedInputStream extends InputStream {

    private final InputStream in;  //包装的io

    private final long max;  //提供的最大长度

    private long pos = 0;  //已经返回的字节数

    private long mark = -1;  //标记

    private boolean propagateClose = true;  //是否关闭

    //一些设计糟糕的api,如返回-1为流结束
    public BoundedInputStream(InputStream in, long size) {
        this.max = size;
        this.in = in;
    }

    /**
     * create new BoundedInputStream,并给定wraps input,且长度是无限的。
     */
    public BoundedInputStream(InputStream in) {
        this(in, -1);
    }



    @Override
    public int read() throws IOException {
        return 0;
    }
}
