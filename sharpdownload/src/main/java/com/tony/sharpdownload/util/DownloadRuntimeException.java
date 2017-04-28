package com.tony.sharpdownload.util;

/**
 * @author Tony
 * @since 2017/4/19 21:14
 */
public class DownloadRuntimeException extends RuntimeException {
    public static final String DOWNLOAD_RUNTIME_EXCEPTION_PREFIX = "Sharp Download Exception";

    public DownloadRuntimeException(String detailMessage){
        super(DOWNLOAD_RUNTIME_EXCEPTION_PREFIX + detailMessage);
    }

    public DownloadRuntimeException(String detailMessage,Throwable throwable){
        super(DOWNLOAD_RUNTIME_EXCEPTION_PREFIX + detailMessage,throwable);
    }
}
