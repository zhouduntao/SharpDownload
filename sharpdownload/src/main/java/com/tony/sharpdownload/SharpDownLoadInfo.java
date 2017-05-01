package com.tony.sharpdownload;

import android.os.Handler;
import android.os.Looper;

import java.util.Observable;

/**
 * @author Tony
 * @version 1.0
 *          <p><strong>Features draft description.主要功能介绍<></p>
 * @since 2017/4/19 21:36
 */
public class SharpDownLoadInfo extends Observable{

    public TaskLevel taskLevel = TaskLevel.MIDDLE;
    public String url;
    public String filePath;
    public String method = Constant.Methcd.GET;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public Exception e;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int progress;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (this.status != status) {
            this.status = status;
        }
        notifyObservers();    }

    public int status;


    public TaskLevel getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(TaskLevel taskLevel) {
        this.taskLevel = taskLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                SharpDownLoadInfo.super.notifyObservers(SharpDownLoadInfo.this);
            }
        });

    }

    public enum TaskLevel {
        HIGHT, MIDDLE, LOW,
    }
}
