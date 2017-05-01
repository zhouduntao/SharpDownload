package com.tony.sharpdownload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Tony
 * @since 2017/4/20 9:33
 */
public class HttpURLNetwork implements SharpDownloadNetwork {

    private HttpURLConnection mConn;

    public HttpURLNetwork() {
    }


    @Override
    public void connect(SharpDownLoadInfo downLoadInfo) throws IOException {
        URL url = new URL(downLoadInfo.getUrl());
        if (mConn == null) {
            mConn = (HttpURLConnection) url.openConnection();
        }
        long alreadyLen = 0;
        File file = new File(downLoadInfo.filePath);
        if (file.exists()) {
            alreadyLen = file.length();
        }
        mConn.addRequestProperty("range", "bytes=" + alreadyLen + "-");
        mConn.setRequestMethod(downLoadInfo.getMethod());
        mConn.connect();
    }

    @Override
    public int getContentLenght(){
        return mConn.getContentLength();
    }


    @Override
    public int getStatusCode() throws IOException {
        if (mConn != null) {
            return mConn.getResponseCode();
        }
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (mConn != null) {
            return mConn.getInputStream();
        }
        return null;
    }

    @Override
    public void disConnect() {
        if (mConn != null) {
            mConn.disconnect();
        }
    }
}
