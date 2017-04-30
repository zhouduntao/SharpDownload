package com.tony.sharpdownload;

import android.os.Handler;
import android.os.Looper;

import com.tony.sharpdownload.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;

/**
 * @author Tony
 * @version 1.0
 * @since 2017/4/19 21:37
 */
public class SharpDownloadRunner extends Observable implements Runnable {

    private final SharpDownloadNetwork mStack;
    private final SharpDownLoadInfo mDownloadInfo;
    private boolean mIsStop = false;

    public SharpDownloadRunner(SharpDownloadNetwork stack, SharpDownLoadInfo info) {
        mStack = stack;
        mDownloadInfo = info;
    }

    @Override
    public void run() {
        OutputStream out = null;
        try {
            mStack.connect(mDownloadInfo);
            if (checkStop()) {
                return;
            }
            int statusCode = mStack.getStatusCode();
            if (statusCode == 206) {
                InputStream in = mStack.getInputStream();
                File file = new File(mDownloadInfo.filePath);
                long alreadyLength = 0;
                if (file.exists()) {
                    alreadyLength = file.length();
                }
                out = new FileOutputStream(file, true);
                byte[] buff = new byte[2048];
                int len;
                long progress = alreadyLength;
                while ((len = in.read(buff)) != -1) {
                    if (checkStop()) {
                        return;
                    }
                    out.write(buff, 0, len);
                    progress += len;
                    mDownloadInfo.setStatus(SharpDownloadStatus.DOWNLOADING);
                    mDownloadInfo.setProgress((int) progress);
                    notifyObservers();
                }
                mDownloadInfo.setStatus(SharpDownloadStatus.FINISH);
                out.close();
                notifyObservers();
            } else {
                mDownloadInfo.setStatus(SharpDownloadStatus.ERROR);
                notifyObservers();
            }
            mStack.disConnect();
            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadInfo.setStatus(SharpDownloadStatus.ERROR);
            mDownloadInfo.e = e;
            notifyObservers();
        } finally {
            IOUtils.close(out);
        }
    }

    private boolean checkStop() {
        if (mIsStop) {
            mDownloadInfo.setStatus(SharpDownloadStatus.PAUSE);
            notifyObservers();
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
        notifyObservers();
        return this;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SharpDownloadRunner.super.notifyObservers(mDownloadInfo);
            }
        });
    }
}
