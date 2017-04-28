package com.tony.sharpdownload;

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
        if (mConn == null){
            mConn = (HttpURLConnection) url.openConnection();
        }
        mConn.addRequestProperty("range", "bytes=" + 0 + "-");
        mConn.setRequestMethod("GET");
        mConn.connect();
    }

    @Override
    public int getstatusCode() throws IOException {
        if (mConn != null){
            return mConn.getResponseCode();
        }
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (mConn != null){
            return mConn.getInputStream();
        }
        return null;
    }

    @Override
    public void disConnect() {
        if (mConn != null){
            mConn.disconnect();
        }
    }
}
