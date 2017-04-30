package com.tony.sharpdownload;

import android.content.Context;

import com.tony.sharpdownload.util.DownloadRuntimeException;
import com.tony.sharpdownload.util.ThreadUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author zhouduntao
 * @since 2017/4/19 21:01
 */
public class SharpDownloadManager implements Observer {
    private Context sContext;
    private SharpDownloadNetwork mNetwork;
    private HashMap<SharpDownLoadInfo, SharpDownloadRunner> mHm;
    private final static SharpDownloadManager mInstance = new SharpDownloadManager();
    private LinkedList<SharpDownLoadInfo> mRuningList = new LinkedList<>();
    private LinkedList<SharpDownLoadInfo> mWaitList = new LinkedList<>();
    private int mMaxSize = 3;

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

        if (sContext == null) {
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
        if (mRuningList.contains(downLoadInfo) || mWaitList.contains(downLoadInfo)) {
            return;
        }

        if (mRuningList.size() <= mMaxSize) {
            addRunner(downLoadInfo);
        } else {
            switch (downLoadInfo.taskLevel) {
                case HIGHT:
                    SharpDownLoadInfo removerDownloadInfo = mRuningList.removeFirst();
                    SharpDownloadRunner runner = mHm.get(removerDownloadInfo);
                    runner.cancel();
                    mRuningList.remove(runner);
                    mWaitList.addFirst(removerDownloadInfo);
                    addRunner(downLoadInfo);
                    break;
                case MIDDLE:
                    mWaitList.addFirst(downLoadInfo);
                    break;
                case LOW:
                    mWaitList.addLast(downLoadInfo);
                    break;
            }
        }

    }

    private void addRunner(SharpDownLoadInfo downLoadInfo) {
        SharpDownloadRunner runner = new SharpDownloadRunner(new HttpURLNetwork(), downLoadInfo);
        runner.addObserver(this);
        mHm.put(downLoadInfo, runner);
        mRuningList.add(downLoadInfo);
        ThreadUtils.run(runner);
    }

    public void addObserver(SharpDownLoadInfo downLoadInfo, Observer observer) {
        mHm.get(downLoadInfo).addObserver(observer);
    }

    public void removeObserver(SharpDownLoadInfo downLoadInfo, Observer observer) {
        mHm.get(downLoadInfo).deleteObserver(observer);
    }

    public void pause(SharpDownLoadInfo downLoadInfo) {
        mHm.get(downLoadInfo).pause();
    }

    public void cancel(SharpDownLoadInfo downLoadInfo) {
        mHm.get(downLoadInfo).cancel();
    }

    @Override
    public void update(Observable o, Object arg) {
        SharpDownLoadInfo downLoadInfo = (SharpDownLoadInfo) arg;
        switch (downLoadInfo.status) {
            case SharpDownloadStatus.FINISH:
            case SharpDownloadStatus.ERROR:
            case SharpDownloadStatus.PAUSE:
                mRuningList.remove(arg);
                if (mWaitList.size()  > 0){
                    addRunner(mWaitList.removeFirst());
                }
                break;

        }
    }
}
