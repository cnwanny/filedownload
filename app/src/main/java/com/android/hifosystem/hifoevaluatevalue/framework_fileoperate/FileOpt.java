package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.psnl.hzq.tool.TimeEx;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 类名 ：FileOpt.java
 * 功能 ：
 * 作者 ： wanny
 * 时间 ：2015年6月10日上午10:00:30
 */
public class FileOpt {

    /**
     * 功能 ： 判断是不是文件存在
     * 传递参数 ：
     * 返回类型 ：boolean
     * 作者 ： wanny
     * 时间 ：2015年6月10日上午10:06:38
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 功能 ： 将数据写到这个路径对应的文件中
     * 传递参数 ：
     * 返回类型 ：void
     * 作者 ： wanny
     * 时间 ：2015年5月19日上午10:42:28
     */
    public static void writeFileData(String path, String name, String data) {
        try {
            File file = new File(path, name);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能 ： 生成图片文件名称
     * 传递参数 ：
     * 返回类型 ：String
     * 作者 ： wanny
     * 时间 ：2015年5月19日上午10:46:48
     */
    public static String remotName() {
        return "sign_image" + TimeEx.getStringTime14() + ".jpg";
    }

    /**
     * 功能 ： 读取缓存的文件转换成String
     * 传递参数 ：
     * 返回类型 ：String
     * 作者 ： wanny
     * 时间 ：2015年5月19日上午10:46:48
     */
    public static String readFileData(String filePath) {
        String result = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                return null;
            FileInputStream fis = new FileInputStream(file);
            int buffersize = fis.available();
            byte[] buffer = new byte[buffersize];
            fis.read(buffer);
            fis.close();
            result = new String(buffer);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 功能 ： 删除指定文件文件
     * 传递参数 ：
     * 返回类型 ：boolean
     * 作者 ： wanny
     * 时间 ：2015年6月10日上午10:07:39
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        try {
            File file = new File(path);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }
    }

    /**
     * 功能 ： 删除文件下面的跟文件
     * 传递参数 ：
     * 返回类型 ：void
     * 作者 ： wanny
     * 时间 ：2015年6月10日上午10:07:53
     */
    public static void RecursionDeleteFile(File file) {
        //如果是文件的话
        if (file.isFile()) {
            file.delete();
            return;
        }
        //如果文件夹
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 功能 ： 将Bitmap转换成文件
     * 传递参数 ：
     * 返回类型 ：boolean
     * 作者 ： wanny
     * 时间 ：2015年6月10日上午10:00:35
     */
    public synchronized static boolean writeImage(String path, Bitmap bitmap) {
        boolean ret = false;
        if (StoragePath.storageStatus) {
            File file = new File(path);
            BufferedOutputStream bos;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();
                ret = true;
            } catch (Exception e1) {
                file.delete();
                e1.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param
     * @return
     */
    public static List<File> getFileSort(List<File> list) {
        // List<File> list = getFiles(path, new ArrayList<File>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() > newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }

                }
            });

        }

        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 初始化保存图片的路径
     * @return
     */
    public static String initPath(){
        if(TextUtils.isEmpty(StoragePath.photoDir.toString())){
            StoragePath.createDirs();
        }
        return   StoragePath.photoDir;
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    public static String saveBitmap(Bitmap b){

        String path = initPath();
        //图片保存的本地路径
        String imagePath = path + "/" + TimeEx.getStringTime14() +".jpg";
        LogUtil.log("saveBitmap:jpegName = " + imagePath);
        try {
            FileOutputStream fout = new FileOutputStream(imagePath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            LogUtil.log("saveBitmap成功");
            //数据保存成功后
            return imagePath;
        } catch (IOException e) {
            LogUtil.log("saveBitmap:失败");
            e.printStackTrace();
            return "";
        }

    }
}
