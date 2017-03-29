package com.a17gp.filedownlibs.operate;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.a17gp.filedownlibs.listener.DownOperateImp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件名： HttpDownManager
 * 功能：
 * 作者： wanny
 * 时间： 14:48 2017/2/22
 */
public class HttpDownManager {

    /*停止下载*/
    static final int STOP = 0x0005;
    /*开始下载*/
    static final int START = 0x0003;
    /*正在下载*/
    static final int LOADING = 0x0002;
    /*保存本地成功*/
    static final int SAVELOCAL = 0x0001;
    /*下载出现异常*/
    static final int ERROR = 0x0004;
    /*下载完成*/
    static final int SUCCESS = 0x0006;

    //创建单例模式
    volatile static HttpDownManager instance;

    public static HttpDownManager getInstance() {
        if (instance == null) {
            synchronized (HttpDownManager.class) {
                instance = new HttpDownManager();
            }
        }
        return instance;
    }


    //目前在做的是单文件的下载操作；
    //还是在线程中启动文件的下载就是在下载的过程中要涉及到对UI的更新操作
    private DownFileInfo downFileInfo;
    private boolean canDown = false;

    private UIOperateHandler mHandler;

    //设置更新页面的操作；
    public UIOperateHandler getmHandler() {
        return mHandler;
    }

    public void setmHandler(UIOperateHandler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * 停止下载
     */
    public void stopDown() {
        canDown = false;
    }

    public void startDown(DownFileInfo downFileInfo) {
        canDown = true;
        this.downFileInfo = downFileInfo;
        Message message = new Message();
        message.what = HttpDownManager.START;
        mHandler.sendMessage(message);
        new DownLoadThread().start();
        //开始启动线程来执行下载操作
    }


    public class DownLoadThread extends Thread {
        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();
            if (!TextUtils.isEmpty(downFileInfo.getSavePath())) {
                int downnum = 0;//已下载的大小
                int downcount = 0;//下载百分比
                try {
                    URL url = new URL(downFileInfo.getUrl());
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    byte[] bytes = new byte[8 * 1024];
                    int totalLength = httpURLConnection.getContentLength();
                    OutputStream outputStream = new FileOutputStream(downFileInfo.getSavePath());
                    // 开始读取
                    int readsize = 0;
                    while (((readsize = inputStream.read(bytes)) != -1) && canDown) {
                        outputStream.write(bytes, 0, readsize);
                        //计算加载的百分数
                        downnum += readsize;
                        //正在下载
                        if ((downcount == 0) || (int) (downnum * 100 / totalLength) - 1 > downcount) {
                            downcount += 1;
                            Message msg = new Message();
                            msg.what = HttpDownManager.LOADING;
                            downFileInfo.setProgress((int) downnum * 100 / totalLength);//
                            msg.obj = downFileInfo;
                            Log.d("当前的进度是==", (int) downnum * 100 / totalLength + "");
                            mHandler.sendMessage(msg);
                        }
//                           //标示已经完成下载
                        if (downnum == totalLength) {
                            Message msg = new Message();
                            msg.what = HttpDownManager.SUCCESS;
                            mHandler.sendMessage(msg);
                        }
                    }
                    outputStream.close();
                    inputStream.close();
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = HttpDownManager.SAVELOCAL;
                        msg.obj = downFileInfo;
                        mHandler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = HttpDownManager.ERROR;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = HttpDownManager.ERROR;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }
    }

}
