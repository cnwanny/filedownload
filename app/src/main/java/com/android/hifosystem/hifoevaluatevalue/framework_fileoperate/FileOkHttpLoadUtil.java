package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.ImageUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.android.hifosystem.hifoevaluatevalue.framework_net.retrofit.ApiStores;
import com.psnl.hzq.tool.ListQue;
import com.psnl.hzq.tool.TimeEx;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 类名： FileOkHttpLoadUtil
 * 工鞥： 附件上传功能，包括单文件上传和
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class FileOkHttpLoadUtil {

    private Context context;

    private ArrayList<LocalFileEntity> uploadFile;

    public FileOkHttpLoadUtil(Context context) {
        this.context = context;
    }

    private String localPath;

    public FileOkHttpLoadUtil(Context contex, String localPath) {
        this.context = contex;
        this.localPath = localPath;
    }

    public FileOkHttpLoadUtil(Context context, ArrayList<LocalFileEntity> uploadFile) {
        this.context = context;
        this.uploadFile = uploadFile;

    }

    /**
     * 让线程能够运行
     */
    public void setRunState() {
        complete = true;
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public ArrayList<LocalFileEntity> getUploadFile() {
        return uploadFile;
    }

    /**
     * 设置上传的数据集合
     *
     * @param uploadFile
     */
    public void setUploadFile(ArrayList<LocalFileEntity> uploadFile) {
        this.uploadFile = uploadFile;
    }

    //上传操作
    private boolean complete = true;
    //创建要上传数据的队列
    private ListQue<LocalFileEntity> quefile;

    public boolean getStatus() {
        return complete;
    }

    /**
     * 上传附件启动线程
     */
    private String fid = "";
    public void uoloadFile() {
        quefile = new ListQue<>();
        quefile.clear();
        for (LocalFileEntity entity : uploadFile) {
            fid = entity.getFID();
            quefile.push(entity);
        }
        //创建队列
        if (quefile.getSize() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (complete) {
                        LogUtil.log("线程中当前的状态==", complete + "");
                        uploading();
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    LogUtil.log("已经完成附件的上传", "");
                    Intent intent = new Intent();
                    intent.putExtra("pid",fid);
                    intent.setAction(AppContent.UploadSuccess);
                    context.sendBroadcast(intent);
                    LogUtil.log("发送完成广播", "完成附件的上传");
                }
            }).start();
        }
    }

    //当前的位置
    private int position = 0;

    /**
     * 线程中执行耗时的操作来上传附件
     */
    private void uploading() {
        if (quefile.getSize() > 0) {
            LogUtil.log("quesize==" + quefile.getSize());
            final LocalFileEntity entity = quefile.pop();
            try {
                if (entity != null) {
                    if (uploadSuccessInterface != null) {
                        uploadSuccessInterface.startUpload(entity, position);
                    }
                    String result = "";
                    File file = new File(entity.getLocalFilePath());
                    FileInputStream fis = new FileInputStream(file);
                    LogUtil.log("图片原始大小", fis.getChannel().size() / 1024 + "KB", null);
                    if (fis.getChannel().size() / 1024 > 500) {
                        fis.close();
                        //产权证不需要压缩
//                        if (!TextUtils.isEmpty(entity.getCategory()) && entity.getCategory().equals("视频")) {
//                            result = startOkHttp(entity.getLocalFileName(), file);
//                            LogUtil.log("权属信息，不需要压缩");
//                        } else {
                        if (isImg(entity.getLocalFilePath())) {
                            result = createFileByByte(entity.getLocalFilePath(), 720, 1280, 500);
                        }
                    } else {
                        //开始上传
                        fis.close();
                        result = startOkHttp(entity.getLocalFilePath(), file);
                    }
                    LogUtil.log("上传成功路径 ==" + result);
                    position++;
                    if (uploadSuccessInterface != null) {
                        if (complete) {
                            LogUtil.log("上传成功执行回调" + result);
                            uploadSuccessInterface.uploadsuccess(entity, result, position);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            complete = false;
        }
    }

    //    NetWordInterface.UpLoadFilePath
    private Request setRequset(String filename, File file) {
        Request request = new Request.Builder()
                .url(ApiStores.UpLoadFilePath + "fileName" + "=" + filename)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        return request;
    }

    /**
     * 停止当前的线程，清除队列
     */
    public void stopUpLoad() {
        this.complete = false;
        if (quefile != null && quefile.getSize() > 0) {
            quefile.clear();
        }
        LogUtil.log("状态==", complete + "");
    }


    /**
     * 执行上传操作
     *
     * @param filename
     * @param file
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws IllegalStateException
     */
    private String startOkHttp(String filename, File file) throws IOException, JSONException, IllegalStateException {
        String result = "";
        Request request = setRequset(filename, file);
        Response response = OkHttpUtils.execute(request);
        if (response.isSuccessful()) {
            String value = response.body().string();
            JSONObject jsonObject = new JSONObject(value);
            if (jsonObject.has("Status")) {
                if (jsonObject.getBoolean("Status")) {
                    if (jsonObject.has("Result")) {
                        result = jsonObject.getString("Result");
                        LogUtil.log("路径", result);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 将流文件转换成64的编码
     *
     * @param fis
     * @return
     * @throws Exception
     */
    private String getByte64ByFile(FileInputStream fis) throws Exception {
        String uploadBuffer;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = fis.read(buffer)) >= 0) {
            baos.write(buffer, 0, count);
        }
        uploadBuffer = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.flush();
        baos.close();
        fis.close();
        return uploadBuffer;
    }

    /**
     * 对图片的压缩
     *
     * @param image
     * @param size
     * @return
     */
    private ByteArrayOutputStream compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        LogUtil.log("开始压缩是图片大小==", baos.toByteArray().length / 1024 + "KB", null);
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少5
            LogUtil.log("循环压缩中文件的大小==", baos.toByteArray().length / 1024 + "KB", null);
        }
        // 回收Bitmap added by wang 20150525
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        // end
        LogUtil.log("压缩完成后的附件大小==", baos.toByteArray().length / 1024 + "KB", null);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }

    /**
     * 功能 ： 计算图片的尺寸大小以及缩放比例
     * 传递参数 ：
     * 返回类型 ：Bitmap
     * 作者 ： wanny
     * 时间 ：2015年6月11日上午11:31:19
     */


    private String createFileByByte(String path, float width, float height, int size) throws Exception {
        String result;
        ByteArrayOutputStream bos = compressImage(ImageUtils.getBitmapByWidth(path, width, height), size);
        //创建路径
        if (TextUtils.isEmpty(StoragePath.cacheDir)) {
            StoragePath.createDirs();
        }
        LogUtil.log("提交时文件大小==", bos.toByteArray().length / 1024 + "KB", null);
        String tempName = StoragePath.cacheDir + "/" + TimeEx.getStringTime14() + path.substring(path.lastIndexOf("."), path.length());
        //创建临时文件缓存路径
        File temfile = new File(tempName);
        //然后写入到
        FileOutputStream fos = new FileOutputStream(temfile);
        bos.writeTo(fos);
        fos.close();
        //经过压缩后的图片路径
        result = startOkHttp(tempName.substring(tempName.lastIndexOf("/") + 1,tempName.length()), temfile);
        if (temfile.exists()) {
            temfile.delete();
        }
        return result;
    }

    private boolean isImg(String filepath) {
        if (filepath != null) {
            int typeindex = filepath.lastIndexOf(".");
            if (typeindex != -1) {
                String filetype = filepath.substring(typeindex + 1).toLowerCase();
                LogUtil.log("filetype:" + filetype);
                if (filetype != null) {
                    if (filetype.equals("jpg") || filetype.equals("gif") || filetype.equals("png")
                            || filetype.equals("jpeg") || filetype.equals("bmp") || filetype.equals("wbmp")
                            || filetype.equals("ico") || filetype.equals("jpe")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //启动单文件上传
    public void upLoadSingleFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(localPath);
                    if (file.exists()) {
                        String result = createFileByByte(localPath, 240, 360, 50);
//                        String filename = localPath.substring(localPath.lastIndexOf("/") + 1,localPath.length());
//                        String result = startOkHttp(filename,file);
                        if (!TextUtils.isEmpty(result) && result.contains(".")) {
                            if (uploadSuccessInterface != null) {
                                uploadSuccessInterface.uploadsuccess(null, result, 0);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //上传经度回调
    private UploadSuccessInterface uploadSuccessInterface;

    public void setUploadSuccessInterface(UploadSuccessInterface uploadSuccessInterface) {
        this.uploadSuccessInterface = uploadSuccessInterface;
    }

    //停止当前的操作回调
    private StopUploadInterface stopUploadInterface;

    public void setStopUploadInterface(StopUploadInterface stopUploadInterface) {
        this.stopUploadInterface = stopUploadInterface;
    }

}
