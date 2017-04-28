package com.tony.sharpdownload.util;

/**
 * @author Tony
 * @version 1.0
 * @since 2017/4/28 16:30
 */
public class ThreadUtils {
    private static ThreadProxy sNormalProxy;
    private static ThreadProxy sDownloadProxy;

    public static void run(Runnable r) {
        if (sNormalProxy == null) {
            synchronized (ThreadUtils.class) {
                if (sNormalProxy == null){
                    ThreadProxy.Builder builder = new ThreadProxy.Builder();
                    builder.maximumPoolSize = 3;
                    builder.corePoolSize = 3;
                    sNormalProxy = builder.build();
                }
            }
        }
        sNormalProxy.run(r);
    }

    public static void runAsDownload(Runnable r){
        if (sDownloadProxy == null) {
            synchronized (ThreadUtils.class) {
                if (sDownloadProxy == null){
                    sDownloadProxy = new ThreadProxy.Builder().build();
                }
            }
        }
        sDownloadProxy.run(r);
    }
}
