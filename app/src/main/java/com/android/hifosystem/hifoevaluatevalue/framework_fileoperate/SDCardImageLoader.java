
package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.widget.ImageView;


import com.android.hifosystem.hifoevaluatevalue.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 从SDCard异步加载图片
 *
 * @author hanj 2013-8-22 19:25:46
 */

public class SDCardImageLoader {
    //缓存到内存，缓存到硬盘上去。
    private LruCache<String, Bitmap> imageCache;
    // 固定2个线程来执行任务
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    //实例化 handler更新数据
    private Handler handler = new Handler();

    private int screenW, screenH;


    public SDCardImageLoader() {
        setLoadSize();
    }

    public SDCardImageLoader(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;
        // 获取应用程序最大可用内存
        setLoadSize();
    }


    private void setLoadSize() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // 设置图片缓存大小为程序最大可用内存的1/8
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }


/**
     * 加载图片通过缩放比例,相对于屏幕的比例
     * @param smallRate
     * @param filePath
     * @param callback
     * @return
     */


    private Bitmap loadDrawable(final int smallRate, final String filePath,
                                final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据

        if (imageCache.get(filePath) != null) {
            return imageCache.get(filePath);
        }

        // 如果缓存没有则读取SD卡
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, opt);

                    // 获取到这个图片的原始宽度和高度
                    int picWidth = opt.outWidth;
                    int picHeight = opt.outHeight;
                    //读取图片失败时直接返回
                    if (picWidth == 0 || picHeight == 0) {
                        return;
                    }
                    //初始压缩比例
                    opt.inSampleSize = smallRate;
                    // 根据屏的大小和图片大小计算出缩放比例
                    if (picWidth > picHeight) {
                        if (picWidth > screenW)
                            opt.inSampleSize *= picWidth / screenW;
                    } else {
                        if (picHeight > screenH)
                            opt.inSampleSize *= picHeight / screenH;
                    }
                    //这次再真正地生成一个有像素的，经过缩放了的bitmap
                    opt.inJustDecodeBounds = false;
                    final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);
                    //存入map
                    if (bmp != null && filePath != null) {
                        imageCache.put(filePath, bmp);
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(bmp);
//                            if(!bmp.isRecycled()){
//                                bmp.recycle();
//                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }


/**
     * 通过特定的高度和宽度来实现图片的压缩
     *
     * @param width
     * @param height
     * @param filePath
     * @param callback
     * @return
     */


    private Bitmap loadBitmapBySize(final int width, final int height, final String filePath,
                                    final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据

        if (imageCache.get(filePath) != null) {
            return imageCache.get(filePath);
        }

        // 如果缓存没有则读取SD卡
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, opt);

                    // 获取到这个图片的原始宽度和高度
                    int picWidth = opt.outWidth;
                    int picHeight = opt.outHeight;
                    //读取图片失败时直接返回
                    if (picWidth == 0 || picHeight == 0) {
                        return;
                    }

                    //初始压缩比例
                    opt.inSampleSize = 1;
                    // 根据屏的大小和图片大小计算出缩放比例
                    if (picWidth > picHeight) {
                        if (picWidth > width)
                            opt.inSampleSize = picWidth / width;
                    } else {
                        if (picHeight > height)
                            opt.inSampleSize = picHeight / height;
                    }
                    //这次再真正地生成一个有像素的，经过缩放了的bitmap
                    opt.inJustDecodeBounds = false;
                    final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);
                    //存入map
                    if (bmp != null && filePath != null) {
                        imageCache.put(filePath, bmp);
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(bmp);
//                            if(!bmp.isRecycled()){
//                                bmp.recycle();
//                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }



/**
     * 异步读取SD卡图片，并按指定的比例进行压缩（最大不超过屏幕像素数）
     *
     * @param smallRate 压缩比例，不压缩时输入1，此时将按屏幕像素数进行输出
     * @param filePath  图片在SD卡的全路径
     * @param imageView 组件
     */

    public void loadImage(int smallRate, final String filePath, final ImageView imageView) {

        Bitmap bmp = loadDrawable(smallRate, filePath, new ImageCallback() {

            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.icon_empty);
                    }
                }
            }
        });

        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.icon_empty);
        }

    }



/**
     * @param width
     * @param height
     * @param filePath
     * @param imageView
     */

    public void loadImageByMediaPath(final int width, final int height, final String filePath, final ImageView imageView) {

        Bitmap bmp = loadBitmapByMedia(width,height, filePath, new ImageCallback() {

            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.icon_empty);
                    }
                }
//                if (imageView.getTag().equals(filePath)) {
//                    if (bmp != null) {
//                        imageView.setImageBitmap(bmp);
//                    } else {
//                        imageView.setImageResource(R.drawable.icon_empty);
//                    }
//                }
            }
        });

        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.icon_empty);
        }

    }


/**
     * 通过视频路径来获取视频所落土
     * @param width
     * @param height
     * @param filePath
     * @param callback
     * @return
     */

    private Bitmap loadBitmapByMedia(final int width , final int height, final String filePath,
                                     final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据
        if (imageCache.get(filePath) != null) {
            return imageCache.get(filePath);
        }
        // 如果缓存没有则读取SD卡
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    // 获取视频的缩略图
                   Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
                   final Bitmap resultbitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    if(bitmap != null && !bitmap.isRecycled()){
                        bitmap.recycle();
                    }
                    //存入map
                    if (resultbitmap != null && filePath != null) {
                        imageCache.put(filePath, resultbitmap);
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(resultbitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }


    public void loadImageBySize(int newWidth, int newHeight, final String filePath, final ImageView imageView) {
        Bitmap bmp = loadBitmapBySize(newWidth, newHeight, filePath, new ImageCallback() {
            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.icon_empty);
                    }
                }
            }
        });
        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.icon_empty);
        }
    }


    // 对外界开放的回调接口
    public interface ImageCallback {
        // 注意 此方法是用来设置目标对象的图像资源
        void imageLoaded(Bitmap imageDrawable);
    }


//    public static ImageLoader initImageLoader(Context context, ImageLoader imageLoader, String dirName) {
//
//        imageLoader = ImageLoader.getInstance();
//        if (imageLoader.isInited()) {
//            // 重新初始化ImageLoader时,需要释放资源.
//            imageLoader.destroy();
//        }
//        imageLoader.init(initImageLoaderConfig(context, dirName));
//        return imageLoader;
//    }
//
//
/**
//     * 配置图片下载器
//     *
//     * @param dirName 文件名
//     */

//    private static ImageLoaderConfiguration initImageLoaderConfig(
//            Context context, String dirName) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)
//                .threadPoolSize(3).memoryCacheSize(getMemoryCacheSize(context))
//                .denyCacheImageMultipleSizesInMemory()
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        return config;
//    }

    private static int getMemoryCacheSize(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // icon_switch_thumb/8 of app memory
            // limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }
        return memoryCacheSize;
    }


}

