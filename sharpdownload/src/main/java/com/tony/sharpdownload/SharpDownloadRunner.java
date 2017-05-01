package com.tony.sharpdownload;

import com.tony.sharpdownload.util.DownloadRuntimeException;
import com.tony.sharpdownload.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tony
 * @version 1.0
 * @since 2017/4/19 21:37
 */
public class SharpDownloadRunner implements Runnable {

    private final SharpDownloadNetwork mNetwork;
    private final SharpDownLoadInfo mDownloadInfo;
    private boolean mIsStop = false;

    public SharpDownloadRunner(SharpDownloadNetwork stack, SharpDownLoadInfo info) {
        mNetwork = stack;
        mDownloadInfo = info;
    }

    @Override
    public void run() {
        OutputStream out = null;
        try {
            mNetwork.connect(mDownloadInfo);
            if (checkStop()) {
                return;
            }
            int statusCode = mNetwork.getStatusCode();
            if (statusCode == 206) {
                InputStream in = mNetwork.getInputStream();
                File file = new File(mDownloadInfo.filePath);
                long alreadyLength = 0;
                if (file.exists()) {
                    alreadyLength = file.length();
                } else {

                }
                long totalLenght = mNetwork.getContentLenght() + alreadyLength;
                out = new FileOutputStream(file, true);
                byte[] buff = new byte[2048];
                int len;
                while ((len = in.read(buff)) != -1) {
                    if (checkStop()) {
                        return;
                    }
                    out.write(buff, 0, len);
                    alreadyLength += len;
                    int progress = (int) (((alreadyLength * 100) / totalLenght) + .5f);
                    mDownloadInfo.setProgress(progress);
                    mDownloadInfo.setStatus(SharpDownloadStatus.DOWNLOADING);
                }
                mDownloadInfo.setStatus(SharpDownloadStatus.FINISH);
                out.close();
            } else if (statusCode == 416) {
                mDownloadInfo.setStatus(SharpDownloadStatus.FINISH);
            } else {
                mDownloadInfo.setE(new DownloadRuntimeException("net work error: status code:" + statusCode));
                mDownloadInfo.setStatus(SharpDownloadStatus.ERROR);
            }
            mNetwork.disConnect();
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadInfo.setStatus(SharpDownloadStatus.ERROR);
            mDownloadInfo.e = e;
        } finally {
            IOUtils.close(out);
        }
    }

    private boolean checkStop() {
        if (mIsStop) {
            mDownloadInfo.setStatus(SharpDownloadStatus.PAUSE);
            return true;
        }
        return false;
    }

    public void pause() {
        mIsStop = true;
    }

    public SharpDownloadRunner cancel() {
        pause();
        File file = new File(mDownloadInfo.filePath);
        if (file.exists()) {
            file.delete();
        }
        mDownloadInfo.setStatus(SharpDownloadStatus.CANCEL);
        return this;
    }

}
