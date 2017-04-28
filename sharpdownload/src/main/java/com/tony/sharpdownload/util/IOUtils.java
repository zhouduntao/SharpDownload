package com.tony.sharpdownload.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author zhouduntao
 * @since 2017/4/28 16:02
 */
public class IOUtils {

    public static void close(Closeable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
