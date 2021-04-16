package com.mro.RAR.model;

import java.io.Serializable;

/**
 * 多部分上传一个部分的返回结果
 */
public class PartETag implements Serializable {
    private static final long serialVersionUID = 2471854027355307627L;

    private int partNumber;
    private String eTag;
    private long partSize;
    private Long partCRC;

    /**
     * Constructor
     *
     * @param partNumber
     *            Part number.
     * @param eTag
     *            Part ETag.
     */
    public PartETag(int partNumber, String eTag) {
        this.partNumber = partNumber;
        this.eTag = eTag;
    }

    /**
     * Constructor
     *
     * @param partNumber
     *            Part number.
     * @param eTag
     *            Part ETag.
     * @param partSize
     *            Part Size.
     * @param partCRC
     *            Part's CRC value.
     */
    public PartETag(int partNumber, String eTag, long partSize, Long partCRC) {
        this.partNumber = partNumber;
        this.eTag = eTag;
        this.partSize = partSize;
        this.partCRC = partCRC;
    }

    /**
     * Gets part number.
     *
     * @return Part number.
     */
    public int getPartNumber() {
        return partNumber;
    }

    /**
     * Sets part number.
     *
     * @param partNumber
     *            Part number.
     */
    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * Gets the part's ETag
     *
     * @return Part ETag.
     */
    public String getETag() {
        return eTag;
    }

    /**
     * Sets the part's ETag.
     *
     * @param eTag
     *            Part ETag.
     */
    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public long getPartSize() {
        return partSize;
    }

    public void setPartSize(long partSize) {
        this.partSize = partSize;
    }

    public Long getPartCRC() {
        return partCRC;
    }

    public void setPartCRC(Long partCRC) {
        this.partCRC = partCRC;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eTag == null) ? 0 : eTag.hashCode());
        result = prime * result + partNumber;
        return result;
    }

}

