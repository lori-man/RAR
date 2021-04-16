package com.mro.RAR.common.utils;

public interface RARHeaders extends HttpHeaders{
    static final String RAR_PREFIX = "x-rar-";
    static final String RAR_USER_METADATA_PREFIX = "x-rar-meta-";

    static final String RAR_CANNED_ACL = "x-rar-acl";
    static final String STORAGE_CLASS = "x-rar-storage-class";
    static final String RAR_VERSION_ID = "x-rar-version-id";

    static final String RAR_SERVER_SIDE_ENCRYPTION = "x-rar-server-side-encryption";
    static final String RAR_SERVER_SIDE_ENCRYPTION_KEY_ID = "x-rar-server-side-encryption-key-id";

    static final String GET_OBJECT_IF_MODIFIED_SINCE = "If-Modified-Since";
    static final String GET_OBJECT_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    static final String GET_OBJECT_IF_MATCH = "If-Match";
    static final String GET_OBJECT_IF_NONE_MATCH = "If-None-Match";

    static final String HEAD_OBJECT_IF_MODIFIED_SINCE = "If-Modified-Since";
    static final String HEAD_OBJECT_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    static final String HEAD_OBJECT_IF_MATCH = "If-Match";
    static final String HEAD_OBJECT_IF_NONE_MATCH = "If-None-Match";

    static final String COPY_OBJECT_SOURCE = "x-rar-copy-source";
    static final String COPY_SOURCE_RANGE = "x-rar-copy-source-range";
    static final String COPY_OBJECT_SOURCE_IF_MATCH = "x-rar-copy-source-if-match";
    static final String COPY_OBJECT_SOURCE_IF_NONE_MATCH = "x-rar-copy-source-if-none-match";
    static final String COPY_OBJECT_SOURCE_IF_UNMODIFIED_SINCE = "x-rar-copy-source-if-unmodified-since";
    static final String COPY_OBJECT_SOURCE_IF_MODIFIED_SINCE = "x-rar-copy-source-if-modified-since";
    static final String COPY_OBJECT_METADATA_DIRECTIVE = "x-rar-metadata-directive";
    static final String COPY_OBJECT_TAGGING_DIRECTIVE = "x-rar-tagging-directive";

    static final String RAR_HEADER_REQUEST_ID = "x-rar-request-id";
    static final String RAR_HEADER_VERSION_ID = "x-rar-version-id";

    static final String ORIGIN = "origin";
    static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    static final String ACCESS_CONTROL_REQUEST_HEADER = "Access-Control-Request-Headers";

    static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    static final String RAR_SECURITY_TOKEN = "x-rar-security-token";

    static final String RAR_NEXT_APPEND_POSITION = "x-rar-next-append-position";
    static final String RAR_HASH_CRC64_ECMA = "x-rar-hash-crc64ecma";
    static final String RAR_OBJECT_TYPE = "x-rar-object-type";

    static final String RAR_OBJECT_ACL = "x-rar-object-acl";

    static final String RAR_HEADER_CALLBACK = "x-rar-callback";
    static final String RAR_HEADER_CALLBACK_VAR = "x-rar-callback-var";
    static final String RAR_HEADER_SYMLINK_TARGET = "x-rar-symlink-target";

    static final String RAR_STORAGE_CLASS = "x-rar-storage-class";
    static final String RAR_RESTORE = "x-rar-restore";
    static final String RAR_ONGOING_RESTORE = "ongoing-request=\"true\"";

    static final String RAR_BUCKET_REGION = "x-rar-bucket-region";

    static final String RAR_SELECT_PREFIX = "x-rar-select";
    static final String RAR_SELECT_CSV_ROWS = RAR_SELECT_PREFIX + "-csv-rows";
    static final String RAR_SELECT_OUTPUT_RAW = RAR_SELECT_PREFIX + "-output-raw";
    static final String RAR_SELECT_CSV_SPLITS = RAR_SELECT_PREFIX + "-csv-splits";
    static final String RAR_SELECT_INPUT_LINE_RANGE = RAR_SELECT_PREFIX + "-line-range";
    static final String RAR_SELECT_INPUT_SPLIT_RANGE = RAR_SELECT_PREFIX + "-split-range";

    static final String RAR_TAGGING = "x-rar-tagging";

    static final String RAR_REQUEST_PAYER = "x-rar-request-payer";

    static final String RAR_HEADER_TRAFFIC_LIMIT = "x-rar-traffic-limit";

    static final String RAR_HEADER_TASK_ID = "x-rar-task-id";
}
