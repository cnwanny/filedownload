package com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 类名 ：PhotoInfoEntity.java
 * 功能 ：@本地图片信息
 * 作者 ： wanny
 * 时间 ：2015年8月6日上午10:02:56
 */
public class PhotoInfoEntity implements Parcelable {
    //图片的的名称
    private String name;
    //图片的路径
    private String path_local;
    //图片类型,后缀名
    private String type;

    private String CategoryName;
    private int CategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath_local() {
        return path_local;
    }

    public void setPath_local(String path_local) {
        this.path_local = path_local;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path_local);
        dest.writeString(this.type);
        dest.writeString(this.CategoryName);
        dest.writeInt(this.CategoryId);
    }

    public PhotoInfoEntity() {
    }

    protected PhotoInfoEntity(Parcel in) {
        this.name = in.readString();
        this.path_local = in.readString();
        this.type = in.readString();
        this.CategoryName = in.readString();
        this.CategoryId = in.readInt();
    }

    public static final Creator<PhotoInfoEntity> CREATOR = new Creator<PhotoInfoEntity>() {
        @Override
        public PhotoInfoEntity createFromParcel(Parcel source) {
            return new PhotoInfoEntity(source);
        }

        @Override
        public PhotoInfoEntity[] newArray(int size) {
            return new PhotoInfoEntity[size];
        }
    };
}
