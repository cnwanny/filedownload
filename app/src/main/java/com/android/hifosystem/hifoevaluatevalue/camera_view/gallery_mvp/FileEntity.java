package com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名： FileEntity
 * 功能：
 * 作者： wanny
 * 时间： 20:33 2016/8/22
 */
public class FileEntity implements Parcelable {

    private int ID;//": 0,
    private String FileName;//": "string",
    private int CategoryId;//": 0,
    private String CategoryName;//": "string",
    private String FastPath;
    private String FID;//":"string",
    private String CreateTime;//":"2016-08-22T07:07:10.797Z",
    private String UserAccount;//":"string",
    private String UserName;//":"string",
    private boolean IsDeleted;//":true,
    private long   FileSize;//":0,
    private String Extension;//":"string"

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getFastPath() {
        return FastPath;
    }

    public void setFastPath(String fastPath) {
        FastPath = fastPath;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    public long getFileSize() {
        return FileSize;
    }

    public void setFileSize(long fileSize) {
        FileSize = fileSize;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }


    public FileEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.FileName);
        dest.writeInt(this.CategoryId);
        dest.writeString(this.CategoryName);
        dest.writeString(this.FastPath);
        dest.writeString(this.FID);
        dest.writeString(this.CreateTime);
        dest.writeString(this.UserAccount);
        dest.writeString(this.UserName);
        dest.writeByte(this.IsDeleted ? (byte) 1 : (byte) 0);
        dest.writeLong(this.FileSize);
        dest.writeString(this.Extension);
    }

    protected FileEntity(Parcel in) {
        this.ID = in.readInt();
        this.FileName = in.readString();
        this.CategoryId = in.readInt();
        this.CategoryName = in.readString();
        this.FastPath = in.readString();
        this.FID = in.readString();
        this.CreateTime = in.readString();
        this.UserAccount = in.readString();
        this.UserName = in.readString();
        this.IsDeleted = in.readByte() != 0;
        this.FileSize = in.readLong();
        this.Extension = in.readString();
    }

    public static final Creator<FileEntity> CREATOR = new Creator<FileEntity>() {
        @Override
        public FileEntity createFromParcel(Parcel source) {
            return new FileEntity(source);
        }

        @Override
        public FileEntity[] newArray(int size) {
            return new FileEntity[size];
        }
    };
}
