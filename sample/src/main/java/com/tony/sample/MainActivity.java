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

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    private Button mDownLoadBtn1;
    private Button mDownLoadBtn2;
    private Button mDownLoadBtn3;

    private TextView mDownLoadInfoTv1;
    private TextView mDownLoadInfoTv2;
    private TextView mDownLoadInfoTv3;

    private SharpDownLoadInfo mInfo1;
    private SharpDownLoadInfo mInfo2;
    private SharpDownLoadInfo mInfo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharpDownloadManager.get().init(this);

        mDownLoadBtn1 = (Button) findViewById(R.id.download_button1);
        mDownLoadBtn2 = (Button) findViewById(R.id.download_button2);
        mDownLoadBtn3 = (Button) findViewById(R.id.download_button3);

        mDownLoadInfoTv1 = (TextView) findViewById(R.id.download_info1);
        mDownLoadInfoTv2 = (TextView) findViewById(R.id.download_info2);
        mDownLoadInfoTv3 = (TextView) findViewById(R.id.download_info3);


        mDownLoadBtn1.setOnClickListener(this);
        mDownLoadBtn2.setOnClickListener(this);
        mDownLoadBtn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        File directory = Environment.getExternalStorageDirectory();
        switch (v.getId()) {
            case R.id.download_button1:
                mInfo1 = new SharpDownLoadInfo();
                mInfo1.setTargetFile(directory.getAbsolutePath() + "sharp1");
                String url1 = "http://duntao.win/download/1.apk";
                mInfo1.setUrl(url1);
                SharpDownloadManager.get().enqueue(mInfo1);
                SharpDownloadManager.get().addObserver(mInfo1, this);
                break;
            case R.id.download_button2:
                mInfo1 = new SharpDownLoadInfo();
                mInfo1.setTargetFile(directory.getAbsolutePath() + "sharp2");
                String url2 = "http://duntao.win/download/1.apk";
                mInfo1.setUrl(url2);
                SharpDownloadManager.get().enqueue(mInfo1);
                SharpDownloadManager.get().addObserver(mInfo1, this);
                break;
            case R.id.download_button3:
                mInfo1 = new SharpDownLoadInfo();
                mInfo1.setTargetFile(directory.getAbsolutePath() + "sharp3");
                String url3 = "http://duntao.win/download/1.apk";
                mInfo1.setUrl(url3);
                SharpDownloadManager.get().enqueue(mInfo1);
                SharpDownloadManager.get().addObserver(mInfo1, this);
                break;
        }

    }

    @Override
    public void update(Observable o, final Object arg) {
        SharpDownLoadInfo info = (SharpDownLoadInfo) arg;
        if (info == mInfo1) {
            switch (info.status){
                case SharpDownloadStatus.ERROR:
                    mDownLoadInfoTv1.setText(info.e.toString());
                    break;
                case SharpDownloadStatus.DOWNLOADING:
                    mDownLoadInfoTv1.setText(info.progress + "");
                    break;
            }
        } else if (info == mInfo2) {
            mDownLoadInfoTv2.setText(info.progress + "");
        } else if (info == mInfo3) {
            mDownLoadInfoTv3.setText(info.progress + "");
        }
    }
}
