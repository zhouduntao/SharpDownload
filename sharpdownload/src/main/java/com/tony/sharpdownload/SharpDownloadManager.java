package com.tony.sharpdownload;

import android.content.Context;

import com.tony.sharpdownload.util.DownloadRuntimeException;
import com.tony.sharpdownload.util.ThreadUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observer;

/**
 * @author zhouduntao
 * @since 2017/4/19 21:01
 */
public class SharpDownloadManager {
    private Context sContext;
    private SharpDownloadNetwork mNetwork;
    private HashMap<SharpDownLoadInfo, SharpDownloadRunner> mHm;
    private final static SharpDownloadManager mInstance = new SharpDownloadManager();
    private LinkedList<Runnable> mRuningList = new LinkedList<>();

    private SharpDownloadManager() {
    }

    public static SharpDownloadManager get() {
        return mInstance;
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, SharpDownloadNetwork downloadStack) {
        if (context == null) {
            new DownloadRuntimeException("context not be null");
            return;
        }

        if (sContext == null){
            sContext = context.getApplicationContext();
        }

        if (downloadStack != null) {
            mNetwork = downloadStack;
        } else {

        }

        if (mHm == null) {
            mHm = new HashMap<>();
        }
    }

    public void enqueue(SharpDownLoadInfo downLoadInfo) {
        switch (downLoadInfo.taskLevel) {
            case HIGHT:
                break;
            case MIDDLE:
                break;
            case LOW:
                break;
        }
        mNetwork = new HttpURLNetwork();
        SharpDownloadRunner runner = new SharpDownloadRunner(mNetwork, downLoadInfo);
        mHm.put(downLoadInfo, runner);
        ThreadUtils.run(runner);
    }

    public void addObserver(SharpDownLoadInfo downLoadInfo, Observer observer) {
        mHm.get(downLoadInfo).addObserver(observer);
    }

    public void removeObserver(SharpDownLoadInfo downLoadInfo, Observer observer) {
        mHm.get(downLoadInfo).deleteObserver(observer);
    }

    public void pause(SharpDownLoadInfo downLoadInfo){
        mHm.get(downLoadInfo).pause();
    }

    public void cancel(SharpDownLoadInfo downLoadInfo){
        mHm.get(downLoadInfo).cancel();
    }
}
