package com.android.hifosystem.hifoevaluatevalue.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 文件名： NewPremissionUtils
 * 功能：  6.0权限申请操作
 * 作者： wanny
 * 时间： 15:55 2016/7/4
 */
public class NewPremissionUtils {

    private Activity context;

    public NewPremissionUtils(Activity context) {
        this.context = context;
    }

    /**
     * 判断是不是要获取新的权限
     *
     * @return
     */
    public boolean hasNeedReqset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写文件的操作
     *
     * @param requestCode
     * @return
     */

    public boolean requestWriteSDCardPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打电话的权限
     *
     * @param requestCode
     * @return
     */
    public boolean requesCallPhonePermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 读取SD卡的东西
     *
     * @param requestCode
     * @return
     */
    public boolean requestReadSDCardPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取相机权限
     *
     * @param requestCode
     * @return
     */
    public boolean requestCamerPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }

    //申请定位和拍照权限；
    public boolean requestLocationCamera(int both, int loactCode, int cameraCode) {
        boolean hasLocation = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean hasCamera = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        if (hasCamera && hasLocation) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, both);
            return false;
        } else if (hasLocation) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    loactCode);
            return false;
        } else if (hasCamera) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    cameraCode);
            return false;
        } else {
            return true;
        }
    }


    /**
     * request SDCard write andr read premission,requset premission of Camera;
     * @param both the flag of sdCard and camera all request;
     * @param cameraCode the flag of camera request;
     * @param sdcordCoed  the flag of sdcord request;
     * @return
     */
    public boolean requestSDCardCameraPremission(int both, int cameraCode, int sdcordCoed) {
        boolean hasLocation = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        boolean hasSDCard = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (hasSDCard && hasLocation) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, both);
            return false;
        } else if (hasLocation) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    cameraCode);
            return false;
        } else if (hasSDCard) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    sdcordCoed);
            return false;
        } else {
            return true;
        }
    }

    /**
     * request SDCard write and read premission,requset premission of Phone;
     * @param both the flag of sdCard and camera all request;
     * @param phone the flag of camera request;
     * @param sdcordCoed  the flag of sdcord request;
     * @return
     */
    public boolean requestSDCardPhonePremission(int both, int phone, int sdcordCoed) {
        boolean hasPhone = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED;
        boolean hasSDCard = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (hasSDCard && hasPhone) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, both);
            return false;
        } else if (hasPhone) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    phone);
            return false;
        } else if (hasSDCard) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    sdcordCoed);
            return false;
        } else {
            return true;
        }
    }

    /**
     * request SDCard write and read premission,requset premission of Phone;
     * @param both the flag of sdCard and camera all request;
     * @param phone the flag of camera request;
     * @param sdcordCoed  the flag of sdcord request;
     * @return
     */

    public boolean requestSDCardPhoneCameraPremission(int both, int phone, int sdcordCoed,int camreaCode) {
        boolean hasPhone = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED;
        boolean hasSDCard = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean hasCamera = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        if (hasSDCard && hasPhone && hasCamera) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, both);
            return false;
        } else if (hasPhone && hasCamera) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA},
                    phone + camreaCode);
            return false;
        } else if (hasSDCard && hasPhone) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                    sdcordCoed + phone);
            return false;
        } else if(hasSDCard && hasCamera) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    camreaCode + sdcordCoed);
            return false;

        }else if(hasSDCard){
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    sdcordCoed);
            return false;
        }else if(hasCamera){
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    camreaCode);
            return false;
        } else if(hasPhone){
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE},
                   phone);
            return false;
        }else{
            return true;
        }
    }



    /**
     * request SDCard write andr read premission,requset premission of Camera;
     * @param both the flag of sdCard and camera all request;
     * @param recodeAudio the flag of camera request;
     * @param sdcordCoed  the flag of sdcord request;
     * @return
     */

    public boolean requestSDCardRecodAudioPremission(int both, int recodeAudio, int sdcordCoed) {
        boolean hasSorage = ContextCompat.checkSelfPermission(context.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean hasAudio = ContextCompat.checkSelfPermission(context.getBaseContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
        if (hasSorage && hasAudio) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, both);
            return false;
        } else if (hasAudio) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    recodeAudio);
            return false;
        } else if (hasSorage) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    sdcordCoed);
            return false;
        } else {
            return true;
        }
    }



    /**
     * 获取手机联系人
     *
     * @param requestCode
     * @return
     */

    public boolean requestReadConstantPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取账户的权限
     *
     * @param requestCode
     * @return
     */
    public boolean requestGET_ACCOUNTSPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestLocationAndPhonePermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }



    /**
     * 位置信息授权
     *
     * @param requestCode
     * @return
     */
    public boolean requestLocationPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }
}
