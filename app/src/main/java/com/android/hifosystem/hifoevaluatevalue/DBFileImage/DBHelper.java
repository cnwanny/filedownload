package com.android.hifosystem.hifoevaluatevalue.DBFileImage;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOptInterface;


/**
 * 类名： DBHelper
 * 工鞥： 创建数据库表
 * 作者： wanny
 * 时间：20160116
 */

public class DBHelper  extends SQLiteOpenHelper {

    //创建数据库名称是hifosurvey
    public DBHelper(Context context) {
        super(context, "assessment.db", null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_image = "CREATE TABLE " + FileOptInterface.FILE_TABLE +
                      "(" + "id integer PRIMARY KEY ," +
                FileOptInterface.FileName + " varchar(32)," +
                FileOptInterface.LocalFilePath + " varchar(32)," +
                FileOptInterface.CategoryId + " integer," +
                FileOptInterface.CategoryName + " varchar(32)," +
                FileOptInterface.FID + " varchar(32)," +
                FileOptInterface.UserAccount + " varchar(32)," +
                FileOptInterface.UserName + " varchar(32)," +
                FileOptInterface.Extension + " varchar(32)" +
                ")";
        db.execSQL(sql_image);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
