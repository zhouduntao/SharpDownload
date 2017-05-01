package com.tony.sharpdownload;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tony
 * @since 2017/4/19 22:07
 */
public interface SharpDownloadNetwork {

    void connect(SharpDownLoadInfo downLoadInfo) throws IOException;

    int getContentLenght();

    int getStatusCode() throws IOException;

    InputStream getInputStream() throws IOException;

    void disConnect();
}
