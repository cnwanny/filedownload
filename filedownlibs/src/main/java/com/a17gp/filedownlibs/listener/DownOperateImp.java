package com.a17gp.filedownlibs.listener;

import com.a17gp.filedownlibs.operate.DownFileInfo;

/**
 * 文件名： DownOperateImp
 * 功能：  文件下载操作，暂停，完成，取消
 * 作者： wanny
 * 时间： 14:41 2017/2/22
 */
public interface DownOperateImp<T> {

    void start();
    void stop();
    void success(T t);
    void fail(String error);
    void saveLocal(T t);
    void loading(DownFileInfo porgress);
}
