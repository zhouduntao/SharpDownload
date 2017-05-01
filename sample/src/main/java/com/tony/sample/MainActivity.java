package com.tony.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tony.sharpdownload.SharpDownLoadInfo;
import com.tony.sharpdownload.SharpDownloadManager;
import com.tony.sharpdownload.SharpDownloadStatus;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    private Button mDownLoadBtn1;
    private Button mDownLoadBtn2;
    private Button mDownLoadBtn3;
    private Button mDownLoadBtn4;
    private Button mDownLoadBtn5;

    private TextView mDownLoadInfoTv1;
    private TextView mDownLoadInfoTv2;
    private TextView mDownLoadInfoTv3;
    private TextView mDownLoadInfoTv4;
    private TextView mDownLoadInfoTv5;

    private SharpDownLoadInfo mInfo1;
    private SharpDownLoadInfo mInfo2;
    private SharpDownLoadInfo mInfo3;
    private SharpDownLoadInfo mInfo4;
    private SharpDownLoadInfo mInfo5;
    private String mDirectory;
    private Button mPauseBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharpDownloadManager.get().init(this);

        mDownLoadBtn1 = (Button) findViewById(R.id.download_button1);
        mDownLoadBtn2 = (Button) findViewById(R.id.download_button2);
        mDownLoadBtn3 = (Button) findViewById(R.id.download_button3);
        mDownLoadBtn4 = (Button) findViewById(R.id.download_button4);
        mDownLoadBtn5 = (Button) findViewById(R.id.download_button5);

        mPauseBtn1 = (Button) findViewById(R.id.pase_button1);

        mDownLoadInfoTv1 = (TextView) findViewById(R.id.download_info1);
        mDownLoadInfoTv2 = (TextView) findViewById(R.id.download_info2);
        mDownLoadInfoTv3 = (TextView) findViewById(R.id.download_info3);
        mDownLoadInfoTv4 = (TextView) findViewById(R.id.download_info4);
        mDownLoadInfoTv5 = (TextView) findViewById(R.id.download_info5);

        mDownLoadBtn1.setOnClickListener(this);
        mDownLoadBtn2.setOnClickListener(this);
        mDownLoadBtn3.setOnClickListener(this);
        mDownLoadBtn4.setOnClickListener(this);
        mDownLoadBtn5.setOnClickListener(this);

        mPauseBtn1.setOnClickListener(this);

        mInfo1 = new SharpDownLoadInfo();
        mInfo2 = new SharpDownLoadInfo();
        mInfo3 = new SharpDownLoadInfo();
        mInfo4 = new SharpDownLoadInfo();
        mInfo5 = new SharpDownLoadInfo();
    }

    @Override
    public void onClick(View v) {
        mDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sharp";
        switch (v.getId()) {
            case R.id.download_button1:
                startDownload("/sharp1",mInfo1);
                break;
            case R.id.download_button2:
                startDownload("/sharp2",mInfo2);
                break;
            case R.id.download_button3:
                startDownload("/sharp3",mInfo3);
                break;
            case R.id.download_button4:
                startDownload("/sharp4",mInfo4);
                break;
            case R.id.download_button5:
                startDownload("/sharp5",mInfo5);
                break;
            case R.id.pase_button1:
                SharpDownloadManager.get().pause(mInfo1);
                break;
        }

    }

    private void startDownload(String path,SharpDownLoadInfo info) {
        info.setFilePath(mDirectory + path);
        info.setTaskLevel(SharpDownLoadInfo.TaskLevel.HIGHT);
        String url1 = "http://duntao.win/download/1.apk";
        info.setUrl(url1);
        SharpDownloadManager.get().enqueue(info);
        SharpDownloadManager.get().addObserver(info, this);
    }

    @Override
    public void update(Observable o, final Object arg) {
        SharpDownLoadInfo info = (SharpDownLoadInfo) arg;
        if (info.filePath.equals(mDirectory + "/sharp1")) {
            handleStatus(mDownLoadInfoTv1, info);
        } else if (info.filePath.equals(mDirectory + "/sharp2")) {
            handleStatus(mDownLoadInfoTv2, info);
        } else if (info.filePath.equals(mDirectory + "/sharp3")) {
            handleStatus(mDownLoadInfoTv3, info);
        } else if (info.filePath.equals(mDirectory + "/sharp4")) {
            handleStatus(mDownLoadInfoTv4, info);
        } else if (info.filePath.equals(mDirectory + "/sharp5")) {
            handleStatus(mDownLoadInfoTv5, info);
        }
    }


    private void handleStatus(TextView textView, SharpDownLoadInfo info) {
        switch (info.status) {
            case SharpDownloadStatus.ERROR:
                textView.setText(info.e.toString());
                break;
            case SharpDownloadStatus.DOWNLOADING:
                textView.setText(info.progress + "");
                break;
            case SharpDownloadStatus.FINISH:
                textView.setText("下载成功:文件地址:" + info.filePath);
                break;
                case SharpDownloadStatus.PAUSE:
                    textView.setText("暂停");
                break;
        }
    }
}
