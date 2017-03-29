package com.android.hifosystem.hifoevaluatevalue.Utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 类名： ImageUtils
 * 工鞥： 图片操作相关，包括压缩，旋转。放大，缩小
 * 作者： wanny
 * 时间： 20160218
 */

public class ImageUtils {


    Context context;
    private int dw = 200;
    private int dh = 200;

    public ImageUtils(Context context) {
        this.context = context;
    }

    /**
     * 参数：Bitmap ，旋转角度 argue
     * 功能：图片旋转等比旋转
     * return: Bitmap
     */

    public static Bitmap getScaleBitmap(Bitmap bitmap, int argue) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) argue);
        Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotaBitmap;
    }
    //将bitmap转换成byte
    public static byte[] bmpToByteArray(Bitmap bitmap,final boolean needRecycle){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        if(needRecycle){
            bitmap.recycle();
        }
        byte[] result = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 获取全部图片地址
     *
     * @return
     */
    public ArrayList<String> listAlldir() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        ArrayList<String> list = new ArrayList<String>();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null,
                null, null);// managedQuery(uri, proj, null, null, null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(0);
            list.add(new File(path).getAbsolutePath());
        }
        return list;
    }

//    public List<FileTraversal> LocalImgFileList() {
//        List<FileTraversal> data = new ArrayList<FileTraversal>();
//        String filename = "";
//        List<String> allimglist = listAlldir();
//        List<String> retulist = new ArrayList<String>();
//        if (allimglist != null) {
//            Set set = new TreeSet();
//            String[] str;
//            for (int i = 0; i < allimglist.size(); i++) {
//                retulist.add(getfileinfo(allimglist.get(i)));
//            }
//            for (int i = 0; i < retulist.size(); i++) {
//                set.add(retulist.get(i));
//            }
//            str = (String[]) set.toArray(new String[0]);
//            for (int i = 0; i < str.length; i++) {
//                filename = str[i];
//                FileTraversal ftl = new FileTraversal();
//                ftl.filename = filename;
//                data.add(ftl);
//            }
//
//            for (int i = 0; i < data.size(); i++) {
//                for (int j = 0; j < allimglist.size(); j++) {
//                    if (data.get(i).filename.equals(getfileinfo(allimglist
//                            .get(j)))) {
//                        data.get(i).filecontent.add(allimglist.get(j));
//                    }
//                }
//            }
//        }
//        return data;
//    }

    // 显示原生图片尺寸大小
    public Bitmap getPathBitmap(Uri imageFilePath, int dw, int dh)
            throws FileNotFoundException {
        // 获取屏幕的宽和高
        /**
         * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片
         * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true
         * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。
         * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值
         */
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // 由于使用了MediaStore存储，这里根据URI获取输入流的形式
        Bitmap pic = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageFilePath), null, op);

        int wRatio = (int) Math.ceil(op.outWidth / (float) dw); // 计算宽度比例
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh); // 计算高度比例

        /**
         * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 如果高和宽不是全都超出了屏幕，那么无需缩放。
         * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 这需要判断wRatio和hRatio的大小
         * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 缩放使用的还是inSampleSize变量
         */
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false; // 注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
        pic = BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(imageFilePath), null, op);

        return pic;
    }

    public String getfileinfo(String data) {
        String filename[] = data.split("/");
        if (filename != null) {
            return filename[filename.length - 2];
        }
        return null;
    }

    /**
     * 且图片压缩。
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */

    public static Bitmap getZoomBitmap(Bitmap bitmap, int width, int height) {
        Bitmap result = null;
        //bitmap的宽度
        int bmpWidth = bitmap.getWidth();
        //bitmap高度
        int bmpHeight = bitmap.getHeight();
        float scaleX = (float) width / bmpWidth;
        float scaleY = (float) height / bmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return result;
    }

    /**
     * 功能：通过文件返回文件的Bitmap
     *
     * @param path
     * @return
     * @throws IOException
     */

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    //	private int loadBitAsynkNumber = 0;
//	private int MAX_NUMBER = 20;
//
//	public boolean imgExcute(ImageView imageView, ImgCallBack icb,
//			String... params) {
//
//		if (loadBitAsynkNumber < MAX_NUMBER) {
//			loadBitAsynkNumber++;
//			LoadBitAsynk loadBitAsynk = new LoadBitAsynk(imageView, icb);
//			loadBitAsynk.execute(params);
//			return true;
//		}else{
//			return false;
//		}
//	}
//
//	public class LoadBitAsynk extends AsyncTask<String, Integer, Bitmap> {
//
//		ImageView imageView;
//		ImgCallBack icb;
//
//		LoadBitAsynk(ImageView imageView, ImgCallBack icb) {
//			this.imageView = imageView;
//			this.icb = icb;
//		}
//
//		@Override
//		protected Bitmap doInBackground(String... params) {
//			Bitmap bitmap = null;
//			try {
//				if (params != null) {
//					for (int i = 0; i < params.length; i++) {
//						bitmap = getPathBitmap(
//								Uri.fromFile(new File(params[i])), 200, 200);
//					}
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			super.onPostExecute(result);
//			loadBitAsynkNumber--;
//			if (result != null) {
//				// imageView.setImageBitmap(result);
//				icb.resultImgCall(imageView, result);
//			}
//		}
//
//	}
//
//

    /**
     * 通过路径获取图，指定宽度和高度的图片的Bitmap
     * @param imgPath
     * @param pixelW
     * @param pixelH
     * @return
     */
    public static Bitmap getBitmapByWidth(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * retrave Bitmap by the image local_path.by the partarl need width and height;
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapByWidth(String path, final int width, final  int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap =   BitmapFactory.decodeFile(path, options);
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;
        if (realWidth == 0 || realWidth == 0) {
             return null;
        }
        //初始压缩比例
        options.inSampleSize = 3;
//        // 根据屏的大小和图片大小计算出缩放比例
//        if (realWidth > realHeight) {
//            if (realHeight > height)
//                options.inSampleSize = realHeight / width;
//        } else {
//            if (realWidth > width)
//                options.inSampleSize = realWidth / width;
//        }
       LogUtil.log("真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比
//      dth / width;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        LogUtil.log("缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }

    private int loadBitAsynkNumber = 0;
    private int MAX_NUMBER = 20;
    public Bitmap resizedBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // create the new Bitmap object
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return resizedBitmap;
    }


    public static Bitmap getBitmapByOrginal(String path) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bm为空
//        in.close();
        options.inJustDecodeBounds = false;

        int w = options.outWidth;
        int h = options.outHeight;
        // 现在主流平板的分辨率，所以高和宽我们设置为
        float hh = 1280;// 这里设置高度为1280f
        float ww = 720f;// 这里设置宽度为800f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / hh);
        }
        if (be <= 1)
            be = 1;
        LogUtil.log("be == " + be);
        options.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        in = new BufferedInputStream(new FileInputStream(new File(path)));
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
}
