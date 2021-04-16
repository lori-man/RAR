package com.mro.RAR.model;

/**
 * The file upload request to start a multipart upload.
 */
public class UploadFileRequest extends WebServiceRequest {

    public UploadFileRequest(String uploadFile, long partSize, int taskNum) {
        this.partSize = partSize;
        this.taskNum = taskNum;
        this.uploadFile = uploadFile;
    }

    public UploadFileRequest(long partSize, int taskNum, boolean enableCheckpoint) {
        this.partSize = partSize;
        this.taskNum = taskNum;
        this.uploadFile = uploadFile;
        this.enableCheckpoint = enableCheckpoint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getPartSize() {
        return partSize;
    }

    public void setPartSize(long partSize) {
        if (partSize < 1024 * 100) {
            this.partSize = 1024 * 100;
        } else {
            this.partSize = partSize;
        }
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        if (taskNum < 1) {
            this.taskNum = 1;
        } else if (taskNum > 1000) {
            this.taskNum = 1000;
        } else {
            this.taskNum = taskNum;
        }
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public void setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public void setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
    }

    public ObjectMetadata getObjectMetadata() {
        return objectMetadata;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Sets traffic limit speed, its unit is bit/s
     */
    public void setTrafficLimit(int trafficLimit) {
        this.trafficLimit = trafficLimit;
    }

    /**
     * Gets traffic limit speed, its unit is bit/s
     * @return traffic limit speed
     */
    public int getTrafficLimit() {
        return trafficLimit;
    }


    //文件名称
    private String key;
    // Part size, by default it's 100KB.
    private long partSize = 1024 * 100;
    // Concurrent parts upload thread count. By default it's 1.
    private int taskNum = 1;
    // The local file path to upload.
    private String uploadFile;
    // Enable the checkpoint
    private boolean enableCheckpoint = false;
    // The checkpoint file's local path.
    private String checkpointFile;
    // The metadata of the target file.
    private ObjectMetadata objectMetadata;
    // callback entry.
    private Callback callback;
    // Traffic limit speed, its uint is bit/s (unused)
    private int trafficLimit;
}
