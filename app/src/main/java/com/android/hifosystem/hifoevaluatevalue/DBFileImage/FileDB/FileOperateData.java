package com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.DBHelper;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;

import java.util.ArrayList;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class FileOperateData {

    private DBHelper dbHelper;

    private static FileOperateData fileOperateData;
    private Context context;
    public static  FileOperateData getInstance(Context context) {
      synchronized (FileOperateData.class){
          if(fileOperateData == null){
             fileOperateData = new  FileOperateData(context);
          }
      }
        return fileOperateData;
    }

    private FileOperateData(Context context) {
        this.context = context.getApplicationContext();
        dbHelper = new DBHelper(this.context);
    }

    /**
     * 添加单条数据
     *
     * @param entity
     */
    public void inserData(LocalFileEntity entity) {
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            addDataOperate(entity, db);
            db.close();
        }
    }

    /**
     * 添加多条数据
     *
     * @param valueList
     */
    public void inserData(ArrayList<LocalFileEntity> valueList) {
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            for (LocalFileEntity entity : valueList) {
                addDataOperate(entity, db);
            }
            db.close();
        }
    }

    /**
     * 添加数据工具
     *
     * @param entity
     * @param db
     */
    private void addDataOperate(LocalFileEntity entity, SQLiteDatabase db) {

        String sql = "insert into " + " " + FileOptInterface.FILE_TABLE +
                "(" +
                FileOptInterface.FileName + "," +
                FileOptInterface.LocalFilePath + "," +
                FileOptInterface.CategoryId + "," +
                FileOptInterface.CategoryName + "," +
                FileOptInterface.FID + "," +
                FileOptInterface.UserAccount + "," +
                FileOptInterface.UserName + "," +
                FileOptInterface.Extension +
                ")" + " values(?,?,?,?,?,?,?,?)";
        LogUtil.log("sql==", sql);
        Object[] value = new Object[]{entity.getFileName(),  entity.getLocalFilePath(),entity.getCategoryId(),
                entity.getCategoryName(), entity.getFID(), entity.getUserAccount(), entity.getUserName(),entity.getExtension()};
        db.execSQL(sql, value);
    }

    /**
     * @return ArrayList<FileEntity>
     */
    public ArrayList<LocalFileEntity> find() {
        ArrayList<LocalFileEntity> datas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FileOptInterface.FILE_TABLE, null);
        while (cursor.moveToNext()) {
            LocalFileEntity data = new LocalFileEntity();
            data.setFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.FileName)));
            data.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
            data.setCategoryId(cursor.getInt(cursor.getColumnIndex(FileOptInterface.CategoryId)));
            data.setCategoryName(cursor.getString(cursor.getColumnIndex(FileOptInterface.CategoryName)));
            data.setFID(cursor.getString(cursor.getColumnIndex(FileOptInterface.FID)));
            data.setUserAccount(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserAccount)));
            data.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
            data.setExtension(cursor.getString(cursor.getColumnIndex(FileOptInterface.Extension)));
            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;
    }

    /**
     * 功能：通过文件名称来获取文件信息,通过文件名了
     *
     * @param fileName
     * @return FileEntity
     */
    public LocalFileEntity find(String fileName) {
        LocalFileEntity data = new LocalFileEntity();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //从这个数据库中找出
        Cursor cursor = db.rawQuery(
                "select * from " + FileOptInterface.FILE_TABLE + " where " +  FileOptInterface.LocalFilePath + " =? ",
                new String[]{fileName});
        if (cursor.moveToFirst()) {
            data.setFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.FileName)));
            data.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
            data.setCategoryId(cursor.getInt(cursor.getColumnIndex(FileOptInterface.CategoryId)));
            data.setCategoryName(cursor.getString(cursor.getColumnIndex(FileOptInterface.CategoryName)));
            data.setFID(cursor.getString(cursor.getColumnIndex(FileOptInterface.FID)));
            data.setUserAccount(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserAccount)));
            data.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
            data.setExtension(cursor.getString(cursor.getColumnIndex(FileOptInterface.Extension)));
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 获取数据通过fid和username
     * @param fid
     * @param username
     * @return
     */
    public ArrayList<LocalFileEntity> find(String fid , String username) {
        ArrayList<LocalFileEntity> datas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //从这个数据库中找出
        Cursor cursor = db.rawQuery(
                "select * from " + FileOptInterface.FILE_TABLE + " where " +  FileOptInterface.FID + " = ? " + " and " + FileOptInterface.UserName + " = ?",
                new String[]{fid,username});
        while (cursor.moveToNext()) {
            LocalFileEntity data = new LocalFileEntity();
            data.setFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.FileName)));
            data.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
            data.setCategoryId(cursor.getInt(cursor.getColumnIndex(FileOptInterface.CategoryId)));
            data.setCategoryName(cursor.getString(cursor.getColumnIndex(FileOptInterface.CategoryName)));
            data.setFID(cursor.getString(cursor.getColumnIndex(FileOptInterface.FID)));
            data.setUserAccount(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserAccount)));
            data.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
            data.setExtension(cursor.getString(cursor.getColumnIndex(FileOptInterface.Extension)));
            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;
    }


//    /**
//     * 通过objectid
//     * @param object
//     * @return ArrayList<FileEntity>
//     */
//
//    public ArrayList<LocalFileEntity> find(String object) {
//        ArrayList<LocalFileEntity> data = new ArrayList<LocalFileEntity>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        //从这个数据库中找出
//        String sql = "select * from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.FID + " = ?" ;
//        LogUtil.log("sql", sql);
//        Cursor cursor = db.rawQuery(sql, new String[]{object});
//        while (cursor.moveToNext()) {
//            LocalFileEntity entity = new LocalFileEntity();
//            entity.setID(cursor.getInt(cursor.getColumnIndex(FileOptInterface.ID)));
//            entity.setFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.FileName)));
//            entity.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
//            entity.setCategoryId(cursor.getInt(cursor.getColumnIndex(FileOptInterface.CategoryId)));
////            entity.setRemoteFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.RemoteFileName)));
//            entity.setCategoryName(cursor.getString(cursor.getColumnIndex(FileOptInterface.CategoryName)));
//            entity.setFID(cursor.getString(cursor.getColumnIndex(FileOptInterface.FID)));
//            entity.setUserAccount(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserAccount)));
//            entity.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
//            entity.setExtension(cursor.getString(cursor.getColumnIndex(FileOptInterface.Extension)));
//            data.add(entity);
//        }
//        cursor.close();
//        db.close();
//        return data;
//    }

    /**
     * 通过objectid 和key来查询对应栏目下的图片
     * @param object
     * @return ArrayList<FileEntity>
     */

    public int findNumber(String object) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //从这个数据库中找出
        int number = 0;
        String sql = "select * from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.FID + " = ?" ;
        LogUtil.log("sql", sql);
        Cursor cursor = db.rawQuery(sql, new String[]{object});
        while (cursor.moveToNext()) {
            number++;
        }
        cursor.close();
        db.close();
        return number;
    }

//    /**
//     * 获取单个对象下面的图片
//     *
//     * @param object 对象id
//     * @param status 未上传的数据
//     * @return
//     */
//    public ArrayList<LocalFileEntity> findByStatus(String object, int status) {
//        ArrayList<LocalFileEntity> data = new ArrayList<LocalFileEntity>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        //从这个数据库中找出
//        String sql = "select * from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.Objectid + " = ?" + " and " + FileOptInterface.Status + " = ?";
//        LogUtil.log("sql", sql);
//        Cursor cursor = db.rawQuery(sql, new String[]{object, String.valueOf(status)});
//        while (cursor.moveToNext()) {
//            LocalFileEntity entity = new LocalFileEntity();
//            entity.setKey(cursor.getInt(cursor.getColumnIndex(FileOptInterface.Key)));
//            entity.setObjectid(cursor.getString(cursor.getColumnIndex(FileOptInterface.Objectid)));
//            entity.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
//            entity.setStatus(cursor.getInt(cursor.getColumnIndex(FileOptInterface.Status)));
////            entity.setRemoteFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.RemoteFileName)));
//            entity.setCategory(cursor.getString(cursor.getColumnIndex(FileOptInterface.Category)));
//            entity.setLocalFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFileName)));
//            entity.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
//            entity.setProjectid(cursor.getString(cursor.getColumnIndex(FileOptInterface.Projectid)));
//            entity.setObjectName(cursor.getString(cursor.getColumnIndex(FileOptInterface.ObjectName)));
//            data.add(entity);
//        }
//        cursor.close();
//        db.close();
//        return data;
//    }

    /**
     * 功能： 删除单挑数据
     *
     * @param fileEntity
     * @return int
     */
    public int deleteOneFile(LocalFileEntity fileEntity) {
        int isSuccess = 0;
        //删除单条数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //创建删除语句
//        String sql = "delete from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.LocalFilePath + " = ? "  + " and " + FileOptInterface.Objectid + " = ? ";
        String whereArgs = FileOptInterface.LocalFilePath + " = ? " + " and " + FileOptInterface.FID + " = ? ";
        //删除条件
        String[] whereCause = new String[]{fileEntity.getLocalFilePath(), String.valueOf(fileEntity.getFID())};
        //执行删除操作
        isSuccess = db.delete(FileOptInterface.FILE_TABLE, whereArgs, whereCause);
        LogUtil.log("删除成功条数==", isSuccess + "");
//        db.execSQL(sql,whereCause);
        db.close();
        return isSuccess;
    }

    /**
     * 通过用户名来获取未上传的图片列表
     *
     * @param userName 当前登录的用户名
//     * @param status   当前的状态 //状态 0表示未已经添加在对象中， 1，表示待上传的，2表示上传成功了的。
     * @return
     */

    public ArrayList<LocalFileEntity> findByUserName(String userName) {
        ArrayList<LocalFileEntity> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select" + " * " + " from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.UserAccount + " = ?" ;
        LogUtil.log("sql==" + sql);
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        while (cursor.moveToNext()) {
            LocalFileEntity data = new LocalFileEntity();
            data.setFileName(cursor.getString(cursor.getColumnIndex(FileOptInterface.FileName)));
            data.setLocalFilePath(cursor.getString(cursor.getColumnIndex(FileOptInterface.LocalFilePath)));
            data.setCategoryId(cursor.getInt(cursor.getColumnIndex(FileOptInterface.CategoryId)));
            data.setCategoryName(cursor.getString(cursor.getColumnIndex(FileOptInterface.CategoryName)));
            data.setFID(cursor.getString(cursor.getColumnIndex(FileOptInterface.FID)));
            data.setUserAccount(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserAccount)));
            data.setUserName(cursor.getString(cursor.getColumnIndex(FileOptInterface.UserName)));
            data.setExtension(cursor.getString(cursor.getColumnIndex(FileOptInterface.Extension)));
            result.add(data);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 删除数据库
     */
    public void deleteAllData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "DELETE FROM " + FileOptInterface.FILE_TABLE;
        db.execSQL(sql);
    }

    /**
     * 查找该对象是否存在图片
     *
     * @param objectid
     * @return
     */
    public boolean findHasImage(String objectid) {
        boolean result = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select" + " * " + " from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.FID + " = ?";
        LogUtil.log("sql==" + sql);
        Cursor cursor = db.rawQuery(sql, new String[]{objectid});
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                result = true;
            } else
                result = false;
        } else
            result = false;

        cursor.close();
        db.close();
        return result;
    }

    /**
     * 获取该对象下的图片数量
     *
     * @param objectId
     * @return
     */
    public int getObjectImageNumber(String objectId) {
        int number = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select" + " * " + " from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.FID + " = ?";
        LogUtil.log("sql==" + sql);
        Cursor cursor = db.rawQuery(sql, new String[]{objectId});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                number++;
            }
        }
        cursor.close();
        db.close();
        return number;
    }

    /**
     * 查勘是不是存在待上传的附件
     *
     * @param username
     * @return
     */
    public boolean findUpLoadImage(String username) {
        boolean result;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select" + " * " + " from " + FileOptInterface.FILE_TABLE + " where " + FileOptInterface.UserName + " = ? ";
        LogUtil.log("sql == ", sql);
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                result = true;
            } else {
                result = false;
            }
        } else
            result = false;
        cursor.close();
        db.close();
        return result;
    }

        /**
     * updateSubmit
     * 更新分类
     *
     */
    public void updateCategory(LocalFileEntity entity) {
        LogUtil.log("开始更新数据");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "update " + FileOptInterface.FILE_TABLE + " set " + FileOptInterface.CategoryId + " = ?" + " , "  + FileOptInterface.CategoryName + " = ? "+ " where " + FileOptInterface.FID + " = ?";
        LogUtil.log("更新数status状态", sql);
        db.execSQL(sql, new Object[]{entity.getCategoryId(),entity.getCategoryName(),entity.getFID()});
        db.close();
        //
        LogUtil.log("数据状态更新完成");
    }



//    /**
//     * updateSubmit
//     * 将要上传成功的数据status更新为1
//     *
//     * @param status
//     * @param objectid, 当前对象的id
//     */
//    public void updateSubmit(int status, String objectid) {
//        LogUtil.log("开始更新数据");
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        String sql = "update " + FileOptInterface.FILE_TABLE + " set " + FileOptInterface.Status + " = ?" + " where " + FileOptInterface.Objectid + " = ?";
//        LogUtil.log("更新数status状态", sql);
//        db.execSQL(sql, new Object[]{status, objectid});
//        db.close();
//        //
//        LogUtil.log("数据状态更新完成");
//
//    }

    /**
     * 判断数据库是不是存在附件
     *
     * @return
     */
    public boolean hasData() {
        boolean result = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select" + " * " + " from " + FileOptInterface.FILE_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst())
            result = true;
        else
            result = false;
        return result;
    }

}
