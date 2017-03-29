package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;

/**
 * 文件名： FilePostPresenterView
 * 功能：
 * 作者： wanny
 * 时间： 13:55 2016/8/26
 */
public interface FilePostPresenterView {
    void filePostSuccess(FilePostModel model, LocalFileEntity localFileEntity);
}
