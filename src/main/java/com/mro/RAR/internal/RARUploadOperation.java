package com.mro.RAR.internal;

import com.mro.RAR.ServiceClient;
import com.mro.RAR.auth.CredentialsProvider;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.utils.RARHeaders;
import com.mro.RAR.model.*;
import com.mro.RAR.model.options.HttpMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.mro.RAR.RARUtils.*;
import static com.mro.RAR.common.utils.CodingUtils.assertParameterNotNull;
import static com.mro.RAR.common.utils.CodingUtils.assertStringNotNullOrEmpty;
import static com.mro.RAR.common.utils.LogUtils.logException;


public class RARUploadOperation extends RAROperation {

    public RARUploadOperation(ServiceClient client, CredentialsProvider credsProvider) {
        super(client, credsProvider);
    }


    public UploadFileResult uploadFile(UploadFileRequest uploadFileRequest) throws Throwable {
        assertParameterNotNull(uploadFileRequest, "uploadFileRequest");

        assertParameterNotNull(uploadFileRequest.getUploadFile(), "uploadFile");

        //使用默认的检查点，在不指定检查点文件的情况下启用检查点。
        if (uploadFileRequest.isEnableCheckpoint()) {
            if (uploadFileRequest.getCheckpointFile() == null || uploadFileRequest.getCheckpointFile().isEmpty()) {
                uploadFileRequest.setCheckpointFile(uploadFileRequest.getUploadFile() + ".ucp");
            }
        }

        return uploadFileWithCheckpoint(uploadFileRequest);
    }

    private UploadFileResult uploadFileWithCheckpoint(UploadFileRequest uploadFileRequest) throws Throwable {

        UploadFileResult uploadFileResult = new UploadFileResult();
        UploadCheckPoint uploadCheckPoint = new UploadCheckPoint();


        //启用检查点，从检查点文件中读取检查点数据。
        if (uploadFileRequest.isEnableCheckpoint()) {
            //检查点文件不存在，或者损坏，需要重新上传整个文件。
            try {
                uploadCheckPoint.load(uploadFileRequest.getCheckpointFile());

            } catch (Exception e) {
                remove(uploadFileRequest.getCheckpointFile());
            }

            //如果上传文件更新,那么重新上传
            if (!uploadCheckPoint.isValid(uploadFileRequest.getUploadFile())) {
                prepare(uploadCheckPoint, uploadFileRequest);
                remove(uploadFileRequest.getCheckpointFile());
            }
        } else {
            // checkPoint 未启用,重新上传
            prepare(uploadCheckPoint, uploadFileRequest);
        }


        //同时上传 part
        ArrayList<PartResult> partResults = upload(uploadCheckPoint, uploadFileRequest);
        for (PartResult partResult : partResults) {
            if (partResult.isFailed()) {
                throw partResult.exception;
            }
        }
        return null;
    }


    private ArrayList<PartResult> upload(UploadCheckPoint uploadCheckPoint, UploadFileRequest uploadFileRequest) {
        ArrayList<PartResult> taskResults = new ArrayList<PartResult>();
        ExecutorService service = Executors.newFixedThreadPool(uploadFileRequest.getTaskNum());
        ArrayList<Future<PartResult>> futures = new ArrayList<Future<PartResult>>();

        //计算待上传数据的大小
        long contentLength = 0;
        for (int i = 0; i < uploadCheckPoint.uploadParts.size(); i++) {
            if (!uploadCheckPoint.uploadParts.get(i).isCompleted) {
                contentLength += uploadCheckPoint.uploadParts.get(i).size;
            }
        }


        //upload parts
        for (int i = 0; i < uploadCheckPoint.uploadParts.size(); i++) {
            if (uploadCheckPoint.uploadParts.get(i).isCompleted) {
                futures.add(service.submit(new Task(i, "upload-" + i, uploadCheckPoint, i, uploadFileRequest, this)));

            }
        }
        return null;
    }

    public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest) {
        assertParameterNotNull(uploadPartRequest, "uploadPartRequest");

        String key = uploadPartRequest.getKey();
        String uploadId = uploadPartRequest.getUploadId();
        assertParameterNotNull(key, "key");
        ensureObjectKeyValid(key);
        assertStringNotNullOrEmpty(uploadId, "uploadId");

        if (uploadPartRequest.getInputStream() == null) {
            throw new IllegalArgumentException(RAR_RESOURCE_MANAGER.getString("MustSetContentStream"));
        }

        return null;
    }


    /**
     * 准备多点上传文件.(需自定义check上传文件的格式)
     */
    private void prepare(UploadCheckPoint uploadCheckPoint, UploadFileRequest uploadFileRequest) {
        uploadCheckPoint.magic = UploadCheckPoint.UPLOAD_MAGIC;
        uploadCheckPoint.uploadFile = uploadFileRequest.getUploadFile();
        uploadCheckPoint.key = uploadFileRequest.getKey();
        uploadCheckPoint.uploadFileStat = FileStat.getFileStat(uploadCheckPoint.uploadFile);
        uploadCheckPoint.uploadParts = splitFile(uploadCheckPoint.uploadFileStat.size, uploadFileRequest.getPartSize());
        uploadCheckPoint.partETags = new ArrayList<PartETag>();

        ObjectMetadata metadata = uploadFileRequest.getObjectMetadata();
        if (metadata == null) {
            metadata = new ObjectMetadata();
        }

        if (metadata.getContentType() == null) {
            metadata.setContentType(Mimetypes.getInstance().
                    getMimetype(uploadFileRequest.getUploadFile(),
                            uploadFileRequest.getKey()));
        }

        InitiateMultipartUploadRequest initiateUploadRequest =
                new InitiateMultipartUploadRequest(uploadFileRequest.getKey(), metadata);

        String uploadId = initiateMultipartUpload(initiateUploadRequest);

        uploadCheckPoint.uploadID = uploadId;
    }

    /**
     * 发送初始化 multipart file
     */
    public String initiateMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest) {

        assertParameterNotNull(initiateMultipartUploadRequest, "initiateMultipartUploadRequest");

        String key = initiateMultipartUploadRequest.getKey();
        assertParameterNotNull(key, "key");
        ensureObjectKeyValid(key);

        Map<String, String> headers = initiateMultipartUploadRequest.getHeaders();
        headers.put("key", key);
        if (initiateMultipartUploadRequest.getObjectMetadata() != null) {
            populateRequestMetadata(headers, initiateMultipartUploadRequest.getObjectMetadata());
        }

        //注意，不要将对象的总大小作为InitiateMultipartUpload请求的内容长度发送。
        removeHeader(headers, RARHeaders.CONTENT_LENGTH);

        Map<String, String> params = initiateMultipartUploadRequest.getParameters();
        params.put("uploads", null);

        RequestMessage request = new RARRequestMessageBuilder(getInnerClient())
                .setEndpoint(getEndpoint() + "/v1/cloud/common/checkPoint")
                .setMethod(HttpMethod.POST)
                .setHeaders(headers)
                .setParameters(params)
                .setInputStream(new ByteArrayInputStream(new byte[0])).setInputSize(0)
                .setOriginalRequest(initiateMultipartUploadRequest).build();

        return doOperation(request, responseParsers.initiateMultipartUploadResponseParser, false, null, null);
    }

    /**
     * 分割文件大小
     */
    private ArrayList<UploadPart> splitFile(long fileSize, long partSize) {
        ArrayList<UploadPart> parts = new ArrayList<UploadPart>();

        long partNum = fileSize / partSize;
        if (partNum >= 10000) {
            partSize = fileSize / (10000 - 1);
            partNum = fileSize / partSize;
        }

        for (long i = 0; i < partNum; i++) {
            UploadPart part = new UploadPart();
            part.number = (int) (i + 1);
            part.offset = i * partSize;
            part.size = partSize;
            part.isCompleted = false;
            parts.add(part);
        }

        if (fileSize % partSize > 0) {
            UploadPart part = new UploadPart();
            part.number = parts.size() + 1;
            part.offset = parts.size() * partSize;
            part.size = fileSize % partSize;
            part.isCompleted = false;
            parts.add(part);
        }

        return parts;
    }

    /**
     * 删除文件
     */
    private boolean remove(String filePath) {
        boolean flag = false;
        File file = new File(filePath);

        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }

        return flag;
    }

    static class UploadCheckPoint implements Serializable {

        private static final long serialVersionUID = 5424904565837227164L;

        private static final String UPLOAD_MAGIC = "FE8BB4EA-B593-4FAC-AD7A-2459A36E2E62";

        /**
         * Gets the checkpoint data from the checkpoint file.
         */
        public synchronized void load(String cpFile) throws IOException, ClassNotFoundException {
            FileInputStream fileIn = new FileInputStream(cpFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            UploadCheckPoint ucp = (UploadCheckPoint) in.readObject();
            assign(ucp);
            in.close();
            fileIn.close();
        }

        /**
         * Writes the checkpoint data to the checkpoint file.
         */
        public synchronized void dump(String cpFile) throws IOException {
            this.md5 = hashCode();
            FileOutputStream fileOut = new FileOutputStream(cpFile);
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(this);
            outStream.close();
            fileOut.close();
        }

        /**
         * The part upload complete, update the status.
         *
         * @throws IOException
         */
        public synchronized void update(int partIndex, PartETag partETag, boolean completed) throws IOException {
            partETags.add(partETag);
            uploadParts.get(partIndex).isCompleted = completed;
        }

        /**
         * Check if the local file matches the checkpoint.
         */
        public synchronized boolean isValid(String uploadFile) {
            // 比较checkpoint的magic和md5
            // Compares the magic field in checkpoint and the file's md5.
            if (this.magic == null || !this.magic.equals(UPLOAD_MAGIC) || this.md5 != hashCode()) {
                return false;
            }

            // Checks if the file exists.
            File upload = new File(uploadFile);
            if (!upload.exists()) {
                return false;
            }

            // The file name, size and last modified time must be same as the
            // checkpoint.
            // If any item is changed, return false (re-upload the file).
            if (!this.uploadFile.equals(uploadFile) || this.uploadFileStat.size != upload.length()
                    || this.uploadFileStat.lastModified != upload.lastModified()) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((magic == null) ? 0 : magic.hashCode());
            result = prime * result + ((partETags == null) ? 0 : partETags.hashCode());
            result = prime * result + ((uploadFile == null) ? 0 : uploadFile.hashCode());
            result = prime * result + ((uploadFileStat == null) ? 0 : uploadFileStat.hashCode());
            result = prime * result + ((uploadID == null) ? 0 : uploadID.hashCode());
            result = prime * result + ((uploadParts == null) ? 0 : uploadParts.hashCode());
            return result;
        }

        private void assign(UploadCheckPoint ucp) {
            this.magic = ucp.magic;
            this.md5 = ucp.md5;
            this.uploadFile = ucp.uploadFile;
            this.uploadFileStat = ucp.uploadFileStat;
            this.key = ucp.key;
            this.uploadID = ucp.uploadID;
            this.uploadParts = ucp.uploadParts;
            this.partETags = ucp.partETags;
        }

        public String magic;
        public int md5;
        public String uploadFile;
        public FileStat uploadFileStat;
        public String key;
        public String uploadID;
        public ArrayList<UploadPart> uploadParts;
        public ArrayList<PartETag> partETags;

    }

    static class FileStat implements Serializable {
        private static final long serialVersionUID = -1223810339796425415L;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((digest == null) ? 0 : digest.hashCode());
            result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
            result = prime * result + (int) (size ^ (size >>> 32));
            return result;
        }

        public static FileStat getFileStat(String uploadFile) {
            FileStat fileStat = new FileStat();
            File file = new File(uploadFile);
            fileStat.size = file.length();
            fileStat.lastModified = file.lastModified();
            return fileStat;
        }

        public long size; // file size
        public long lastModified; // file last modified time.
        public String digest; // file content's digest (signature).
    }

    static class UploadPart implements Serializable {
        private static final long serialVersionUID = 6692863980224332199L;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (isCompleted ? 1231 : 1237);
            result = prime * result + number;
            result = prime * result + (int) (offset ^ (offset >>> 32));
            result = prime * result + (int) (size ^ (size >>> 32));
            result = prime * result + (int) (crc ^ (crc >>> 32));
            return result;
        }

        public int number; // part number
        public long offset; // the offset in the file
        public long size; // part size
        public boolean isCompleted; // upload completeness flag.
        public long crc; //part crc
    }

    static class PartResult {

        public PartResult(int number, long offset, long length) {
            this.number = number;
            this.offset = offset;
            this.length = length;
        }

        public PartResult(int number, long offset, long length, long partCRC) {
            this.number = number;
            this.offset = offset;
            this.length = length;
            this.partCRC = partCRC;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public boolean isFailed() {
            return failed;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public Long getPartCRC() {
            return partCRC;
        }

        public void setPartCRC(Long partCRC) {
            this.partCRC = partCRC;
        }

        private int number; // part number
        private long offset; // offset in the file
        private long length; // part size
        private boolean failed; // part upload failure flag
        private Exception exception; // part upload exception
        private Long partCRC;
    }

    static class Task implements Callable<PartResult> {

        public Task(int id, String name, UploadCheckPoint uploadCheckPoint, int partIndex,
                    UploadFileRequest uploadFileRequest, RARUploadOperation rarUploadOperation) {
            this.id = id;
            this.name = name;
            this.uploadCheckPoint = uploadCheckPoint;
            this.partIndex = partIndex;
            this.uploadFileRequest = uploadFileRequest;
            this.rarUploadOperation = rarUploadOperation;
        }

        @Override
        public PartResult call() throws Exception {
            PartResult tr = null;

            try (InputStream instream = new FileInputStream(uploadCheckPoint.uploadFile)) {
                UploadPart uploadPart = uploadCheckPoint.uploadParts.get(partIndex);
                tr = new PartResult(partIndex + 1, uploadPart.offset, uploadPart.size);

                instream.skip(uploadPart.offset);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setKey(uploadFileRequest.getKey());
                uploadPartRequest.setUploadId(uploadCheckPoint.uploadID);
                uploadPartRequest.setPartNumber(uploadPart.number);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(uploadPart.size);

                UploadPartResult uploadPartResult = rarUploadOperation.uploadPart(uploadPartRequest);

                PartETag partETag = new PartETag(uploadPartResult.getPartNumber(), uploadPartResult.getETag());
                uploadCheckPoint.update(partIndex, partETag, true);
                if (uploadFileRequest.isEnableCheckpoint()) {
                    uploadCheckPoint.dump(uploadFileRequest.getCheckpointFile());
                }
            } catch (Exception e) {
                tr.setFailed(true);
                tr.setException(e);
                logException(String.format("Task %d:%s upload part %d failed: ", id, name, partIndex + 1), e);
            }

            return tr;
        }

        private int id;
        private String name;
        private UploadCheckPoint uploadCheckPoint;
        private int partIndex;
        private UploadFileRequest uploadFileRequest;
        private RARUploadOperation rarUploadOperation;
    }
}
