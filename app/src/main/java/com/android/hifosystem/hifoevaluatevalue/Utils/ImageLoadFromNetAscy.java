package com.android.hifosystem.hifoevaluatevalue.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class ImageLoadFromNetAscy extends AsyncTask<Integer,Integer,Bitmap> {

    private String imagepath ;
    private int width;
    private int height;


    private LruCache<String ,Bitmap> lruCache = new LruCache<String, Bitmap>(8);
    public ImageLoadFromNetAscy(String imagepath, int width, int height){
        this.imagepath = imagepath;
        this.width = width;
        this.height = height;
    }

    private ImgCallBack imgCallBack;
    //执行完成后的回调
    public void  setImgCallBack(ImgCallBack imgCallBack){
        this.imgCallBack = imgCallBack;
    }
    //执行获取数据操作
    @Override
    protected Bitmap doInBackground(Integer... params) {
        Bitmap result = null ;
        try{
            URL url = new URL(imagepath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            //缩放4倍后再获取，不然内存溢出
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
//            result = ThumbnailUtils.extractThumbnail(bitmap, 180, 180);
            result = ImageUtils.getZoomBitmap(bitmap,width,height);
            if(bitmap != null){
                if(!bitmap.isRecycled()){
                    bitmap.recycle();
                }
            }
        }catch (Exception e){

        }
        return result;
    }

   //执行完成doInBackground后执行该操作
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            if(imgCallBack != null){
                imgCallBack.resultImgCall(null, bitmap);
            }
        }
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    //对应第二个参数Integer
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
