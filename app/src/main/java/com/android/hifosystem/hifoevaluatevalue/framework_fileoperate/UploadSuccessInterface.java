package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;

/**
 * 类名： UploadSuccessInterface
 * 工鞥： 一张图片上传成功后的回调接口
 * 作者： wanny
 * 时间：${date} ${time}
 */

public interface UploadSuccessInterface {
      //上传完成后的回调
     void uploadsuccess(LocalFileEntity fileEntity, String newremotepath, int position);
     //开始上传
     void startUpload(LocalFileEntity fileEntity, int position);
}
