package com.a17gp.filedownlibs.operate;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.a17gp.filedownlibs.listener.DownOperateImp;

/**
 * 文件名： UIOperateHandler
 * 功能：
 * 作者： wanny
 * 时间： 15:08 2017/2/22
 */
public class UIOperateHandler extends Handler {



    public UIOperateHandler(Looper looper) {
        super(looper);
    }

    private DownOperateImp<DownFileInfo> downOperateImp;
    public UIOperateHandler(DownOperateImp<DownFileInfo> downOperateImp,Looper looper) {
        super(looper);
        this.downOperateImp = downOperateImp;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what == HttpDownManager.ERROR){
            if(downOperateImp != null){
                downOperateImp.fail("下载失败");
            }
        }else if(msg.what == HttpDownManager.START){
            if(downOperateImp != null){
                downOperateImp.start();
            }
        }else if(msg.what == HttpDownManager.STOP){
            if(downOperateImp != null){
                downOperateImp.stop();
            }
        }else if(msg.what == HttpDownManager.SAVELOCAL){
            if(downOperateImp != null){
                if(msg.obj != null && msg.obj instanceof DownFileInfo){
                    downOperateImp.saveLocal((DownFileInfo) msg.obj);
                }
            }
        }else if(msg.what == HttpDownManager.LOADING){
            if(downOperateImp != null){
                if(msg.obj != null && msg.obj instanceof DownFileInfo){
                    downOperateImp.loading((DownFileInfo) msg.obj);
                }
            }
        }
    }

}
